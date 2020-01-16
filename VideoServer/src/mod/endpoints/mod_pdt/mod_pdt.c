/* 
 *
 * mod_pdt.c -- pdt Endpoint Module
 *
 */

#include "pdt_api.h"
#include "mod_pdt.h"
#include "im_api.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#ifdef WIN32
#include "win_iconv.c"
#endif // WIN32
#define FRAME_QUEUE_LEN 3

SWITCH_MODULE_LOAD_FUNCTION(mod_pdt_load);
SWITCH_MODULE_SHUTDOWN_FUNCTION(mod_pdt_shutdown);
SWITCH_MODULE_DEFINITION(mod_pdt, mod_pdt_load, mod_pdt_shutdown, NULL);

static switch_status_t find_non_pdt_bridge(switch_core_session_t *session, switch_core_session_t **br_session, const char **br_uuid);

static switch_endpoint_interface_t *pdt_endpoint_interface = NULL;

globals_t pdt_globals;


typedef enum {
	TFLAG_LINKED = (1 << 0),
	TFLAG_OUTBOUND = (1 << 1),
	TFLAG_WRITE = (1 << 2),
	TFLAG_USEME = (1 << 3),
	TFLAG_BRIDGE = (1 << 4),
	TFLAG_BOWOUT = (1 << 5),
	TFLAG_BLEG = (1 << 6),
	TFLAG_APP = (1 << 7),
	TFLAG_RUNNING_APP = (1 << 8),
	TFLAG_BOWOUT_USED = (1 << 9),
	TFLAG_CLEAR = (1 << 10)
} TFLAGS;


static switch_status_t channel_on_init(switch_core_session_t *session);
static switch_status_t channel_on_hangup(switch_core_session_t *session);
static switch_status_t channel_on_destroy(switch_core_session_t *session);
static switch_status_t channel_on_routing(switch_core_session_t *session);
static switch_status_t channel_on_exchange_media(switch_core_session_t *session);
static switch_status_t channel_on_soft_execute(switch_core_session_t *session);
static switch_call_cause_t channel_outgoing_channel(switch_core_session_t *session, switch_event_t *var_event,
													switch_caller_profile_t *outbound_profile,
													switch_core_session_t **new_session, switch_memory_pool_t **pool, switch_originate_flag_t flags,
													switch_call_cause_t *cancel_cause);
static switch_status_t channel_read_frame(switch_core_session_t *session, switch_frame_t **frame, switch_io_flag_t flags, int stream_id);
static switch_status_t channel_write_frame(switch_core_session_t *session, switch_frame_t *frame, switch_io_flag_t flags, int stream_id);
static switch_status_t channel_kill_channel(switch_core_session_t *session, int sig);

switch_size_t trans(const char *pFromCode, const char *pToCode, const char *pInBuf, size_t iInLen, char *pOutBuf, size_t iOutLen)
{
	size_t iRet = 0;
	//打开字符集转换
	iconv_t hIconv = iconv_open(pToCode, pFromCode);
	if (!hIconv || (int)hIconv == -1) return 0;
	//开始转换
	iRet = iconv(hIconv, (char **)(&pInBuf), &iInLen, &pOutBuf, &iOutLen);
	//关闭字符集转换
	iconv_close(hIconv);
	return (switch_size_t)iOutLen;
}
static void clear_queue(pdt_pvt_t *tech_pvt)
{
	void *pop;
	if (tech_pvt->frame_queue == NULL)
	{
		return;
	}
	while (switch_queue_trypop(tech_pvt->frame_queue, &pop) == SWITCH_STATUS_SUCCESS && pop) {
		switch_frame_t *frame = (switch_frame_t *) pop;
		switch_frame_free(&frame);
	}

}

static switch_status_t tech_init(pdt_pvt_t *tech_pvt, switch_core_session_t *session, switch_codec_t *codec)
{
	const char *iananame = "L16";
	uint32_t rate = 8000;
	uint32_t interval = 20;
	switch_status_t status = SWITCH_STATUS_SUCCESS;
	switch_channel_t *channel = switch_core_session_get_channel(session);
	const switch_codec_implementation_t *read_impl;

	if (codec) {
		iananame = codec->implementation->iananame;
		rate = codec->implementation->samples_per_second;
		interval = codec->implementation->microseconds_per_packet / 1000;
	} else {
		const char *var;
		char *codec_modname = NULL;

		if ((var = switch_channel_get_variable(channel, "pdt_initial_codec"))) {
			char *dup = switch_core_session_strdup(session, var);
			uint32_t bit, channels;
			iananame = switch_parse_codec_buf(dup, &interval, &rate, &bit, &channels, &codec_modname, NULL);
		}
		
	}

	if (switch_core_codec_ready(&tech_pvt->read_codec)) {
		switch_core_codec_destroy(&tech_pvt->read_codec);
	}

	if (switch_core_codec_ready(&tech_pvt->write_codec)) {
		switch_core_codec_destroy(&tech_pvt->write_codec);
	}

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s setup codec %s/%d/%d\n", switch_channel_get_name(channel), iananame, rate,
					  interval);

	status = switch_core_codec_init(&tech_pvt->read_codec,
									iananame,
									NULL,
									NULL,
									rate, interval, 1, SWITCH_CODEC_FLAG_ENCODE | SWITCH_CODEC_FLAG_DECODE, NULL, switch_core_session_get_pool(session));

	if (status != SWITCH_STATUS_SUCCESS || !tech_pvt->read_codec.implementation || !switch_core_codec_ready(&tech_pvt->read_codec)) {
		goto end;
	}

	status = switch_core_codec_init(&tech_pvt->write_codec,
									iananame,
									NULL,
									NULL,
									rate, interval, 1, SWITCH_CODEC_FLAG_ENCODE | SWITCH_CODEC_FLAG_DECODE, NULL, switch_core_session_get_pool(session));


	if (status != SWITCH_STATUS_SUCCESS) {
		switch_core_codec_destroy(&tech_pvt->read_codec);
		goto end;
	}

	tech_pvt->read_frame.data = tech_pvt->databuf;
	tech_pvt->read_frame.buflen = sizeof(tech_pvt->databuf);
	tech_pvt->read_frame.codec = &tech_pvt->read_codec;


	tech_pvt->cng_frame.data = tech_pvt->cng_databuf;
	tech_pvt->cng_frame.buflen = sizeof(tech_pvt->cng_databuf);

	tech_pvt->cng_frame.datalen = 2;

	tech_pvt->bowout_frame_count = (tech_pvt->read_codec.implementation->actual_samples_per_second /
									tech_pvt->read_codec.implementation->samples_per_packet) * 2;

	switch_core_session_set_read_codec(session, &tech_pvt->read_codec);
	switch_core_session_set_write_codec(session, &tech_pvt->write_codec);

	if (tech_pvt->flag_mutex) {
		switch_core_timer_destroy(&tech_pvt->timer);
	}

	read_impl = tech_pvt->read_codec.implementation;

	switch_core_timer_init(&tech_pvt->timer, "soft",
						   read_impl->microseconds_per_packet / 1000, read_impl->samples_per_packet * 4, switch_core_session_get_pool(session));


	if (!tech_pvt->flag_mutex) {
		switch_mutex_init(&tech_pvt->flag_mutex, SWITCH_MUTEX_NESTED, switch_core_session_get_pool(session));
		switch_mutex_init(&tech_pvt->mutex, SWITCH_MUTEX_NESTED, switch_core_session_get_pool(session));
		switch_core_session_set_private(session, tech_pvt);
		switch_queue_create(&tech_pvt->frame_queue, FRAME_QUEUE_LEN, switch_core_session_get_pool(session));
		tech_pvt->session = session;
		tech_pvt->channel = switch_core_session_get_channel(session);
	}

  end:

	return status;
}
switch_status_t pdt_tech_media(pdt_pvt_t *tech_pvt, const char *r_sdp, switch_sdp_type_t sdp_type)
{
	uint8_t match = 0, p = 0;
//	switch_rtp_engine_t *engine = NULL;

	switch_assert(tech_pvt != NULL);
	switch_assert(r_sdp != NULL);

	if (zstr(r_sdp)) {
		return SWITCH_STATUS_FALSE;
	}

	if ((match = switch_core_media_negotiate_sdp(tech_pvt->session, r_sdp, &p, sdp_type))) {
// 		engine = &tech_pvt->smh->engines[SWITCH_MEDIA_TYPE_AUDIO];
// 		engine->adv_sdp_port = tech_pvt->rtp_local_port;
		if (switch_core_media_choose_ports(tech_pvt->session, SWITCH_TRUE, SWITCH_FALSE) != SWITCH_STATUS_SUCCESS) {
			//if (switch_core_media_choose_port(tech_pvt->session, SWITCH_MEDIA_TYPE_AUDIO, 0) != SWITCH_STATUS_SUCCESS) {
			return SWITCH_STATUS_FALSE;
		}

		if (switch_core_media_activate_rtp(tech_pvt->session) != SWITCH_STATUS_SUCCESS) {
			return SWITCH_STATUS_FALSE;
		}
		//if (!switch_channel_test_flag(tech_pvt->channel, CF_ANSWERED)) {
		//	switch_channel_set_variable(tech_pvt->channel, SWITCH_ENDPOINT_DISPOSITION_VARIABLE, "EARLY MEDIA");
		//		switch_channel_mark_pre_answered(tech_pvt->channel);
		//}
		return SWITCH_STATUS_SUCCESS;
	}


	return SWITCH_STATUS_FALSE;
}
/* 
   State methods they get called when the state changes to the specific state 
   returning SWITCH_STATUS_SUCCESS tells the core to execute the standard state method next
   so if you fully implement the state you can return SWITCH_STATUS_FALSE to skip it.
*/
static switch_status_t channel_on_init(switch_core_session_t *session)
{
	switch_status_t status = SWITCH_STATUS_SUCCESS;
	switch_core_session_t *other_session = NULL;
//	const char *call_id = NULL;
	int err = 0;
	char sdp[512] = {0};
	int nRtpPort = pdt_globals.PdtServerVoicePort;

	switch_channel_t *channel = switch_core_session_get_channel(session);
	pdt_pvt_t *tech_pvt = switch_core_session_get_private_class(session, SWITCH_PVT_SECONDARY);
//	switch_channel_mark_ring_ready(tech_pvt->channel);

	if (tech_pvt->ChanNum > 0)
	{
		nRtpPort = pdt_globals.PdtServerVoicePort + (tech_pvt->ChanNum - 1) * 2;
	}
// 	switch_core_media_gen_local_sdp(session, SDP_TYPE_RESPONSE, RtpRemoteIp, nRtpPort, "sendrecv", 0);


	if (tech_pvt->mparams != NULL && tech_pvt->mparams->rtpip != NULL)
	{
		switch_snprintf(sdp, sizeof(sdp), "v=0\r\n"
			"o=- 3743800577 3743800577 IN IP4 %s\r\n"
			"s=PDT\r\n"
			"m=audio %d RTP/AVP 8\r\n"
			"a=rtpmap:8 PCMA/8000/1\r\n"
			"a=ptime:20\r\n"
			"a=sendrecv\r\n"
			"a=framerate:50\r\n"
			"c=IN IP4 %s"
			, pdt_globals.domain_name, nRtpPort, pdt_globals.PdtServerVoiceIP);
	}

	tech_pvt->r_sdp = switch_core_session_strdup(session, sdp);
	switch_channel_set_variable(channel, SWITCH_R_SDP_VARIABLE, sdp);
	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "Remote SDP %s:\n%s\n", switch_channel_get_name(channel), sdp);
	switch_core_media_set_sdp_codec_string(session, sdp, SDP_TYPE_RESPONSE);

	switch_ivr_set_user(session, pdt_globals.user_full_id);

	if (switch_core_session_get_partner(session, &other_session) == SWITCH_STATUS_SUCCESS) {
		switch_channel_t *other_channel = switch_core_session_get_channel(other_session);
		switch_channel_set_variable(other_channel, SWITCH_B_SDP_VARIABLE, sdp);
		switch_core_session_rwunlock(other_session);
	}
	if (pdt_globals.bIsDebugServer)
	{//是调试服务器时。让本地和远程端口不一样。
		nRtpPort = nRtpPort + 2;
	}
	//强制修改本地端口
 	switch_channel_set_variable_printf(channel, "force_local_media_port", "%d", nRtpPort);

//	switch_channel_set_variable_printf(channel, SWITCH_LOCAL_MEDIA_PORT_VARIABLE, "%d", nRtpPort);
// 	switch_channel_set_variable(channel, SWITCH_LOCAL_MEDIA_IP_VARIABLE, tech_pvt->mparams->rtpip);
// 	switch_core_media_recover_session(session);
//	switch_channel_set_variable(tech_pvt->channel, SWITCH_R_SDP_VARIABLE, sdp);

//	if (switch_channel_test_flag(tech_pvt->channel, CF_PROXY_MODE)) {
//		pass_sdp(tech_pvt);
//	}
//	else {
		if (pdt_tech_media(tech_pvt, tech_pvt->r_sdp, SDP_TYPE_RESPONSE) != SWITCH_STATUS_SUCCESS) {
			switch_channel_set_variable(channel, SWITCH_ENDPOINT_DISPOSITION_VARIABLE, "CODEC NEGOTIATION ERROR");
			err = 1;
		}

		if (!err && switch_core_media_activate_rtp(session) != SWITCH_STATUS_SUCCESS) {
			switch_channel_hangup(channel, SWITCH_CAUSE_DESTINATION_OUT_OF_ORDER);
			err = 1;
		}
//	}

	if (!err) {
	
		switch_channel_mark_answered(channel);
	}

	switch_core_session_rwunlock(session);
	return status;
}

static void do_reset(pdt_pvt_t *tech_pvt)
{
	switch_clear_flag_locked(tech_pvt, TFLAG_WRITE);

	switch_mutex_lock(tech_pvt->mutex);
	if (tech_pvt->other_tech_pvt) {
		switch_clear_flag_locked(tech_pvt->other_tech_pvt, TFLAG_WRITE);
	}
	switch_mutex_unlock(tech_pvt->mutex);
}

static switch_status_t channel_on_routing(switch_core_session_t *session)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;
//	const char *app, *arg;

	channel = switch_core_session_get_channel(session);
	assert(channel != NULL);

	tech_pvt = switch_core_session_get_private(session);
	assert(tech_pvt != NULL);

	do_reset(tech_pvt);

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s CHANNEL ROUTING\n", switch_channel_get_name(channel));
// 	
// 	if (switch_test_flag(tech_pvt, TFLAG_RUNNING_APP)) {
// 		switch_clear_flag(tech_pvt, TFLAG_RUNNING_APP);
// 	}
// 
// 	if (switch_test_flag(tech_pvt, TFLAG_APP) && !switch_test_flag(tech_pvt, TFLAG_OUTBOUND) && 
// 		(app = switch_channel_get_variable(channel, "pdt_app"))) {
// 		switch_caller_extension_t *extension = NULL;
// 
// 		switch_clear_flag(tech_pvt, TFLAG_APP);
// 		switch_set_flag(tech_pvt, TFLAG_RUNNING_APP);
// 
// 		arg = switch_channel_get_variable(channel, "pdt_app_arg");
// 		extension = switch_caller_extension_new(session, app, app);
// 		switch_caller_extension_add_application(session, extension, "pre_answer", NULL);
// 		switch_caller_extension_add_application(session, extension, app, arg);
// 
// 		switch_channel_set_caller_extension(channel, extension);
// 		switch_channel_set_state(channel, CS_EXECUTE);
// 		return SWITCH_STATUS_FALSE;
// 	}
// 


	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_execute(switch_core_session_t *session)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;
	switch_caller_extension_t *exten = NULL;
	const char *bowout = NULL;
	int bow = 0;

	channel = switch_core_session_get_channel(session);
	assert(channel != NULL);

	tech_pvt = switch_core_session_get_private(session);
	assert(tech_pvt != NULL);

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s CHANNEL EXECUTE\n", switch_channel_get_name(channel));

	if ((bowout = switch_channel_get_variable(tech_pvt->channel, "pdt_bowout_on_execute")) && switch_true(bowout)) {
		/* pdt_bowout_on_execute variable is set */
		bow = 1;
	} else if ((exten = switch_channel_get_caller_extension(channel))) {
		/* check for bowout flag */
		switch_caller_application_t *app_p;

		for (app_p = exten->applications; app_p; app_p = app_p->next) {
			int32_t flags;

			switch_core_session_get_app_flags(app_p->application_name, &flags);

			if ((flags & SAF_NO_LOOPBACK)) {
				bow = 1;
				break;
			}
		}
	}

	if (bow) {
		switch_core_session_t *other_session = NULL;
		switch_caller_profile_t *cp, *clone;
		const char *other_uuid = NULL;
		switch_event_t *event = NULL;

		switch_set_flag(tech_pvt, TFLAG_BOWOUT);

		if ((find_non_pdt_bridge(tech_pvt->other_session, &other_session, &other_uuid) == SWITCH_STATUS_SUCCESS)) {
			switch_channel_t *other_channel = switch_core_session_get_channel(other_session);

			switch_channel_wait_for_state_timeout(other_channel, CS_EXCHANGE_MEDIA, 5000);

			switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(tech_pvt->session), SWITCH_LOG_INFO, "BOWOUT Replacing pdt channel with real channel: %s\n",
							  switch_channel_get_name(other_channel));

			if (switch_event_create_subclass(&event, SWITCH_EVENT_CUSTOM, "pdt::bowout") == SWITCH_STATUS_SUCCESS) {
				switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "Resigning-UUID", switch_channel_get_uuid(channel));
				switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "Acquired-UUID", switch_channel_get_uuid(other_channel));
				switch_event_fire(&event);
			}

			if ((cp = switch_channel_get_caller_profile(channel))) {
				clone = switch_caller_profile_clone(other_session, cp);
				clone->originator_caller_profile = NULL;
				clone->originatee_caller_profile = NULL;
				switch_channel_set_caller_profile(other_channel, clone);
			}

			switch_channel_caller_extension_masquerade(channel, other_channel, 0);
			switch_channel_set_state(other_channel, CS_RESET);
			switch_channel_wait_for_state(other_channel, NULL, CS_RESET);
			switch_channel_set_state(other_channel, CS_EXECUTE);
			switch_core_session_rwunlock(other_session);
			switch_channel_hangup(channel, SWITCH_CAUSE_NORMAL_UNSPECIFIED);
		}
	}

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_destroy(switch_core_session_t *session)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;
	switch_event_t *vars;

	channel = switch_core_session_get_channel(session);
	switch_assert(channel != NULL);

	tech_pvt = switch_core_session_get_private(session);

	if ((vars = (switch_event_t *) switch_channel_get_private(channel, "__pdt_vars__"))) {
		switch_channel_set_private(channel, "__pdt_vars__", NULL);
		switch_event_destroy(&vars);
	}
	
	if (tech_pvt) {
		switch_core_timer_destroy(&tech_pvt->timer);

		if (switch_core_codec_ready(&tech_pvt->read_codec)) {
			switch_core_codec_destroy(&tech_pvt->read_codec);
		}

		if (switch_core_codec_ready(&tech_pvt->write_codec)) {
			switch_core_codec_destroy(&tech_pvt->write_codec);
		}

		if (tech_pvt->write_frame) {
			switch_frame_free(&tech_pvt->write_frame);
		}

		clear_queue(tech_pvt);
	}


	return SWITCH_STATUS_SUCCESS;
}


static switch_status_t channel_on_hangup(switch_core_session_t *session)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;

	channel = switch_core_session_get_channel(session);
	switch_assert(channel != NULL);
	switch_channel_set_variable(channel, "is_pdt", "1");

	tech_pvt = switch_core_session_get_private(session);
	switch_assert(tech_pvt != NULL);
	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s CHANNEL HANGUP\n", switch_channel_get_name(channel));

	switch_clear_flag_locked(tech_pvt, TFLAG_LINKED);

	switch_mutex_lock(tech_pvt->mutex);

	if (tech_pvt->other_tech_pvt) {
		switch_clear_flag_locked(tech_pvt->other_tech_pvt, TFLAG_LINKED);
		if (tech_pvt->other_tech_pvt->session && tech_pvt->other_tech_pvt->session != tech_pvt->other_session) {
			switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_CRIT, "OTHER SESSION MISMATCH????\n");
			tech_pvt->other_session = tech_pvt->other_tech_pvt->session;
		}
		tech_pvt->other_tech_pvt = NULL;
	}

	if (tech_pvt->other_session) {
		switch_channel_hangup(tech_pvt->other_channel, switch_channel_get_cause(channel));
		switch_core_session_rwunlock(tech_pvt->other_session);
		tech_pvt->other_channel = NULL;
		tech_pvt->other_session = NULL;
	}


	if (switch_channel_var_true(channel, "hangup-from-pdt") == SWITCH_FALSE) 
	{//是APP的主动拆线时，需要发出拆线指令。
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT on_hangup From Other! CalledSSI:%d Mid:%d,ChanNum:%d\n", tech_pvt->CalledSSI, tech_pvt->Mid, tech_pvt->ChanNum);
		if (pdt_globals.bImConnected == SWITCH_TRUE)
		{
			PDT_API_Quit(pdt_globals.AgentNo, tech_pvt->Mid, tech_pvt->ChanNum, 1, 0);//0	拆线	1	退出 		2	强拆  	3	退出常规
		}
		else {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "channel_on_hangup IM Server Not Connected Ignore It ! ImConnected:%d\n", pdt_globals.bImConnected);
			//return;
		}
		
	} else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT on_hangup From PDT! CalledSSI:%d Mid:%d,ChanNum:%d\n", tech_pvt->CalledSSI, tech_pvt->Mid, tech_pvt->ChanNum);

	}
	switch_mutex_unlock(tech_pvt->mutex);
// 	pdt_globals.cur_session = NULL;
// 	pdt_globals.cur_channel = NULL;
	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_kill_channel(switch_core_session_t *session, int sig)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;

	channel = switch_core_session_get_channel(session);
	switch_assert(channel != NULL);


	switch (sig) {
	case SWITCH_SIG_BREAK:
		break;
	case SWITCH_SIG_KILL:
		tech_pvt = switch_core_session_get_private(session);
		switch_assert(tech_pvt != NULL);

		switch_channel_hangup(channel, SWITCH_CAUSE_NORMAL_CLEARING);
		switch_clear_flag_locked(tech_pvt, TFLAG_LINKED);
		switch_mutex_lock(tech_pvt->mutex);
		if (tech_pvt->other_tech_pvt) {
			switch_clear_flag_locked(tech_pvt->other_tech_pvt, TFLAG_LINKED);
		}
		switch_mutex_unlock(tech_pvt->mutex);
		break;
	default:
		break;
	}

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s CHANNEL KILL\n", switch_channel_get_name(channel));

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_soft_execute(switch_core_session_t *session)
{
	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "CHANNEL TRANSMIT\n");
	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_exchange_media(switch_core_session_t *session)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;

	channel = switch_core_session_get_channel(session);
	assert(channel != NULL);

	tech_pvt = switch_core_session_get_private(session);
	assert(tech_pvt != NULL);

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "CHANNEL pdt\n");

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_reset(switch_core_session_t *session)
{
	pdt_pvt_t *tech_pvt = (pdt_pvt_t *) switch_core_session_get_private(session);
	switch_assert(tech_pvt != NULL);

	do_reset(tech_pvt);
	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s RESET\n",
					  switch_channel_get_name(switch_core_session_get_channel(session)));

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_hibernate(switch_core_session_t *session)
{
	switch_assert(switch_core_session_get_private(session));

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "%s HIBERNATE\n",
					  switch_channel_get_name(switch_core_session_get_channel(session)));

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_on_consume_media(switch_core_session_t *session)
{
	switch_channel_t *channel = NULL;
	pdt_pvt_t *tech_pvt = NULL;

	channel = switch_core_session_get_channel(session);
	assert(channel != NULL);

	tech_pvt = switch_core_session_get_private(session);
	assert(tech_pvt != NULL);

	switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG, "CHANNEL CONSUME_MEDIA\n");

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_send_dtmf(switch_core_session_t *session, const switch_dtmf_t *dtmf)
{
	pdt_pvt_t *tech_pvt = NULL;

	tech_pvt = switch_core_session_get_private(session);
	switch_assert(tech_pvt != NULL);

	if (tech_pvt->other_channel) {
		switch_channel_queue_dtmf(tech_pvt->other_channel, dtmf);
	}

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t channel_read_frame(switch_core_session_t *session, switch_frame_t **frame, switch_io_flag_t flags, int stream_id)
{
	switch_status_t status = SWITCH_STATUS_FALSE;

	status = switch_core_media_read_frame(session, frame, flags, stream_id, SWITCH_MEDIA_TYPE_AUDIO);

	return status;

// 	switch_channel_t *channel = NULL;
// 	pdt_private_t *tech_pvt = NULL;
// 	switch_status_t status = SWITCH_STATUS_FALSE;
// 	switch_mutex_t *mutex = NULL;
// 	void *pop = NULL;
// 
// 	channel = switch_core_session_get_channel(session);
// 	switch_assert(channel != NULL);
// 
// 	tech_pvt = switch_core_session_get_private(session);
// 	switch_assert(tech_pvt != NULL);
// 
// 	if (!switch_test_flag(tech_pvt, TFLAG_LINKED)) {
// 		goto end;
// 	}
// 
// 	*frame = NULL;
// 
// 	if (!switch_channel_ready(channel)) {
// 		goto end;
// 	}
// 
// 	switch_core_timer_next(&tech_pvt->timer);
// 
// 	mutex = tech_pvt->mutex;
// 	switch_mutex_lock(mutex);
// 
// 
// 	if (switch_test_flag(tech_pvt, TFLAG_CLEAR)) {
// 		clear_queue(tech_pvt);
// 		switch_clear_flag(tech_pvt, TFLAG_CLEAR);
// 	}
// 
// 	if (switch_queue_trypop(tech_pvt->frame_queue, &pop) == SWITCH_STATUS_SUCCESS && pop) {
// 		if (tech_pvt->write_frame) {
// 			switch_frame_free(&tech_pvt->write_frame);
// 		}
// 		
// 		tech_pvt->write_frame = (switch_frame_t *) pop;
// 
//         switch_clear_flag(tech_pvt->write_frame, SFF_RAW_RTP);
// 		tech_pvt->write_frame->timestamp = 0;                    
// 
// 		tech_pvt->write_frame->codec = &tech_pvt->read_codec;
// 		*frame = tech_pvt->write_frame;
// 		tech_pvt->packet_count++;
// 		switch_clear_flag(tech_pvt->write_frame, SFF_CNG);
// 		tech_pvt->first_cng = 0;
// 	} else {
// 		*frame = &tech_pvt->cng_frame;
// 		tech_pvt->cng_frame.codec = &tech_pvt->read_codec;
// 		tech_pvt->cng_frame.datalen = tech_pvt->read_codec.implementation->decoded_bytes_per_packet;
// 		switch_set_flag((&tech_pvt->cng_frame), SFF_CNG);
// 		if (!tech_pvt->first_cng) {
// 			switch_yield(tech_pvt->read_codec.implementation->samples_per_packet);
// 			tech_pvt->first_cng = 1;
// 		}
// 	}
// 
// 
// 	if (*frame) {
// 		status = SWITCH_STATUS_SUCCESS;
// 	} else {
// 		status = SWITCH_STATUS_FALSE;
// 	}
// 
//   end:
// 
// 	if (mutex) {
// 		switch_mutex_unlock(mutex);
// 	}
// 
// 	return status;
}

static void switch_channel_wait_for_state_or_greater(switch_channel_t *channel, switch_channel_t *other_channel, switch_channel_state_t want_state)
{

	switch_assert(channel);
	
	for (;;) {
		if ((switch_channel_get_state(channel) < CS_HANGUP && 
			 switch_channel_get_state(channel) == switch_channel_get_running_state(channel) && switch_channel_get_running_state(channel) >= want_state) ||
			(other_channel && switch_channel_down_nosig(other_channel)) || switch_channel_down(channel)) {
			break;
		}
		switch_cond_next();
	}
}


static switch_status_t find_non_pdt_bridge(switch_core_session_t *session, switch_core_session_t **br_session, const char **br_uuid)
{
	switch_channel_t *channel = switch_core_session_get_channel(session);
	const char *a_uuid = NULL;
	switch_core_session_t *sp = NULL;


	*br_session = NULL;
	*br_uuid = NULL;

	a_uuid = switch_channel_get_partner_uuid(channel);

	while (a_uuid && (sp = switch_core_session_locate(a_uuid))) {
		if (switch_core_session_check_interface(sp, pdt_endpoint_interface)) {
			pdt_pvt_t *tech_pvt;
			switch_channel_t *spchan = switch_core_session_get_channel(sp);

			switch_channel_wait_for_state_or_greater(spchan, channel, CS_ROUTING);

			if (switch_false(switch_channel_get_variable(spchan, "pdt_bowout"))) break;

			tech_pvt = switch_core_session_get_private(sp);

			if (tech_pvt->other_channel) {
				a_uuid = switch_channel_get_partner_uuid(tech_pvt->other_channel);
			}

			switch_core_session_rwunlock(sp);
			sp = NULL;
		} else {
			break;
		}
	}

	if (sp) {
		*br_session = sp;
		*br_uuid = a_uuid;
		return SWITCH_STATUS_SUCCESS;
	}

	return SWITCH_STATUS_FALSE;

}

static switch_status_t channel_write_frame(switch_core_session_t *session, switch_frame_t *frame, switch_io_flag_t flags, int stream_id)
{
	switch_status_t status = SWITCH_STATUS_SUCCESS;

	status = switch_core_media_write_frame(session, frame, flags, stream_id, SWITCH_MEDIA_TYPE_AUDIO);

	return status;
// 	switch_channel_t *channel = NULL;
// 	pdt_private_t *tech_pvt = NULL;
// 	switch_status_t status = SWITCH_STATUS_FALSE;
// 
// 	channel = switch_core_session_get_channel(session);
// 	switch_assert(channel != NULL);
// 
// 	tech_pvt = switch_core_session_get_private(session);
// 	switch_assert(tech_pvt != NULL);
// 
// 	if (switch_test_flag(frame, SFF_CNG) || 
// 		(switch_test_flag(tech_pvt, TFLAG_BOWOUT) && switch_test_flag(tech_pvt, TFLAG_BOWOUT_USED))) {
// 		switch_core_timer_sync(&tech_pvt->timer);
// 		switch_core_timer_sync(&tech_pvt->other_tech_pvt->timer);
// 		return SWITCH_STATUS_SUCCESS;
// 	}
// 
// 	switch_mutex_lock(tech_pvt->mutex);
// 	if (!switch_test_flag(tech_pvt, TFLAG_BOWOUT) &&
// 		tech_pvt->other_tech_pvt &&
// 		switch_test_flag(tech_pvt, TFLAG_BRIDGE) &&
// 		!switch_test_flag(tech_pvt, TFLAG_BLEG) &&
// 		switch_test_flag(tech_pvt->other_tech_pvt, TFLAG_BRIDGE) &&
// 		switch_channel_test_flag(tech_pvt->channel, CF_BRIDGED) &&
// 		switch_channel_test_flag(tech_pvt->other_channel, CF_BRIDGED) &&
// 		switch_channel_test_flag(tech_pvt->channel, CF_ANSWERED) &&
// 		switch_channel_test_flag(tech_pvt->other_channel, CF_ANSWERED) && --tech_pvt->bowout_frame_count <= 0) {
// 		const char *a_uuid = NULL;
// 		const char *b_uuid = NULL;
// 		const char *vetoa, *vetob;
// 
// 
// 		vetoa = switch_channel_get_variable(tech_pvt->channel, "pdt_bowout");
// 		vetob = switch_channel_get_variable(tech_pvt->other_tech_pvt->channel, "pdt_bowout");
// 
// 		if ((!vetoa || switch_true(vetoa)) && (!vetob || switch_true(vetob))) {
// 			switch_core_session_t *br_a, *br_b;
// 			switch_channel_t *ch_a = NULL, *ch_b = NULL;
// 			int good_to_go = 0;
// 
// 			switch_mutex_unlock(tech_pvt->mutex);
// 			find_non_pdt_bridge(session, &br_a, &a_uuid);
// 			find_non_pdt_bridge(tech_pvt->other_session, &br_b, &b_uuid);
// 			switch_mutex_lock(tech_pvt->mutex);
// 
// 			
// 			if (br_a) {
// 				ch_a = switch_core_session_get_channel(br_a);
// 				switch_core_media_bug_transfer_recordings(session, br_a);
// 			}
// 
// 			if (br_b) {
// 				ch_b = switch_core_session_get_channel(br_b);
// 				switch_core_media_bug_transfer_recordings(tech_pvt->other_session, br_b);
// 			}
// 			
// 			if (ch_a && ch_b && switch_channel_test_flag(ch_a, CF_BRIDGED) && switch_channel_test_flag(ch_b, CF_BRIDGED)) {
// 
// 				switch_set_flag_locked(tech_pvt, TFLAG_BOWOUT);
// 				switch_set_flag_locked(tech_pvt->other_tech_pvt, TFLAG_BOWOUT);
// 
// 				switch_clear_flag_locked(tech_pvt, TFLAG_WRITE);
// 				switch_clear_flag_locked(tech_pvt->other_tech_pvt, TFLAG_WRITE);
// 
// 				switch_set_flag_locked(tech_pvt, TFLAG_BOWOUT_USED);
// 				switch_set_flag_locked(tech_pvt->other_tech_pvt, TFLAG_BOWOUT_USED);
// 
// 				if (a_uuid && b_uuid) {
// 					switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_DEBUG,
// 									  "%s detected bridge on both ends, attempting direct connection.\n", switch_channel_get_name(channel));
// 					
// 					/* channel_masquerade eat your heart out....... */
// 					switch_ivr_uuid_bridge(a_uuid, b_uuid);
// 					good_to_go = 1;
// 					switch_mutex_unlock(tech_pvt->mutex);
// 				}
// 			}
// 
// 			if (br_a) switch_core_session_rwunlock(br_a);
// 			if (br_b) switch_core_session_rwunlock(br_b);
// 			
// 			if (good_to_go) {
// 				return SWITCH_STATUS_SUCCESS;
// 			}
// 
// 		}
// 	}
// 
// 	if (switch_test_flag(tech_pvt, TFLAG_LINKED) && tech_pvt->other_tech_pvt) {
// 		switch_frame_t *clone;
// 		
// 		if (frame->codec->implementation != tech_pvt->write_codec.implementation) {
// 			/* change codecs to match */
// 			tech_init(tech_pvt, session, frame->codec);
// 			tech_init(tech_pvt->other_tech_pvt, tech_pvt->other_session, frame->codec);
// 		}
// 
// 
// 		if (switch_frame_dup(frame, &clone) != SWITCH_STATUS_SUCCESS) {
// 			abort();
// 		}
// 		
// 		if ((status = switch_queue_trypush(tech_pvt->other_tech_pvt->frame_queue, clone)) != SWITCH_STATUS_SUCCESS) { 
// 			clear_queue(tech_pvt->other_tech_pvt);
// 			status = switch_queue_trypush(tech_pvt->other_tech_pvt->frame_queue, clone);
// 		}
// 
// 		if (status == SWITCH_STATUS_SUCCESS) {
// 			switch_set_flag_locked(tech_pvt->other_tech_pvt, TFLAG_WRITE);
// 		} else {
// 			switch_frame_free(&clone);
// 		}
// 
// 		status = SWITCH_STATUS_SUCCESS;
// 	}
// 
// 	switch_mutex_unlock(tech_pvt->mutex);

//	return status;
}

static switch_status_t channel_receive_message(switch_core_session_t *session, switch_core_session_message_t *msg)
{
	switch_channel_t *channel;
	pdt_pvt_t *tech_pvt;
	int done = 1, pass = 0;
	switch_core_session_t *other_session;
	
	channel = switch_core_session_get_channel(session);
	switch_assert(channel != NULL);

	tech_pvt = switch_core_session_get_private(session);
	switch_assert(tech_pvt != NULL);

	switch (msg->message_id) {
	case SWITCH_MESSAGE_INDICATE_ANSWER:
		if (tech_pvt->other_channel && !switch_test_flag(tech_pvt, TFLAG_OUTBOUND)) {
			switch_channel_mark_answered(tech_pvt->other_channel);
		}
		break;
	case SWITCH_MESSAGE_INDICATE_PROGRESS:
		if (tech_pvt->other_channel && !switch_test_flag(tech_pvt, TFLAG_OUTBOUND)) {
			switch_channel_mark_pre_answered(tech_pvt->other_channel);
		}
		break;
	case SWITCH_MESSAGE_INDICATE_RINGING:
		if (tech_pvt->other_channel && !switch_test_flag(tech_pvt, TFLAG_OUTBOUND)) {
			switch_channel_mark_ring_ready(tech_pvt->other_channel);
		}
		break;
	case SWITCH_MESSAGE_INDICATE_BRIDGE:
		{
			switch_set_flag_locked(tech_pvt, TFLAG_BRIDGE);
		}
		break;
	case SWITCH_MESSAGE_INDICATE_UNBRIDGE:
		{
			switch_clear_flag_locked(tech_pvt, TFLAG_BRIDGE);
		}
		break;
	default:
		done = 0;
		break;
	}

// 	switch (msg->message_id) {
// 	case SWITCH_MESSAGE_INDICATE_BRIDGE:
// 	case SWITCH_MESSAGE_INDICATE_UNBRIDGE:
// 	case SWITCH_MESSAGE_INDICATE_AUDIO_SYNC:
// 		{
// 
// 			done = 1;
// 			switch_set_flag(tech_pvt, TFLAG_CLEAR);
// // 			switch_set_flag(tech_pvt->other_tech_pvt, TFLAG_CLEAR);
// 
// 			switch_core_timer_sync(&tech_pvt->timer);
// // 			switch_core_timer_sync(&tech_pvt->other_tech_pvt->timer);
// 		}
// 		break;
// 	default:
// 		break;
// 	}


	switch (msg->message_id) {
	case SWITCH_MESSAGE_INDICATE_DISPLAY:
		if (tech_pvt->other_channel) {

			if (switch_test_flag(tech_pvt, TFLAG_BLEG)) {
				if (!zstr(msg->string_array_arg[0])) {
					switch_channel_set_profile_var(tech_pvt->other_channel, "caller_id_name", msg->string_array_arg[0]);
				}
				
				if (!zstr(msg->string_array_arg[1])) {
					switch_channel_set_profile_var(tech_pvt->other_channel, "caller_id_number", msg->string_array_arg[1]);
				}
			} else {
				if (!zstr(msg->string_array_arg[0])) {
					switch_channel_set_profile_var(tech_pvt->other_channel, "callee_id_name", msg->string_array_arg[0]);
				}
				
				if (!zstr(msg->string_array_arg[1])) {
					switch_channel_set_profile_var(tech_pvt->other_channel, "callee_id_number", msg->string_array_arg[1]);
				}
			}
			
			pass = 1;
		}
		break;
	case SWITCH_MESSAGE_INDICATE_DEFLECT:
		{
			pass = 0;

			if (!zstr(msg->string_arg) && switch_core_session_get_partner(tech_pvt->other_session, &other_session) == SWITCH_STATUS_SUCCESS) {
				char *ext = switch_core_session_strdup(other_session, msg->string_arg);
				char *context = NULL, *dp = NULL;
				
				if ((context = strchr(ext, ' '))) {
					*context++ = '\0';
					
					if ((dp = strchr(context, ' '))) {
						*dp++ = '\0';
					}
				}
				switch_ivr_session_transfer(other_session, ext, context, dp);
				switch_core_session_rwunlock(other_session);
			}
		}
		break;
	default:
		break;
	}

	if (!done && tech_pvt->other_session && (pass || switch_test_flag(tech_pvt, TFLAG_RUNNING_APP))) {
		switch_status_t r = SWITCH_STATUS_FALSE;
		
		if (switch_core_session_get_partner(tech_pvt->other_session, &other_session) == SWITCH_STATUS_SUCCESS) {
			r = switch_core_session_receive_message(other_session, msg);
			switch_core_session_rwunlock(other_session);
		}
		
		return r;
	}
	
	return SWITCH_STATUS_SUCCESS;
}

static void set_media_options(pdt_pvt_t *tech_pvt, pdt_profile_t *profile)
{
//	uint32_t i;
	if (tech_pvt->mparams == NULL)
	{
		return;
	}
	if (!zstr(profile->rtpip[profile->rtpip_cur])) {
		tech_pvt->mparams->rtpip4 = switch_core_session_strdup(tech_pvt->session, profile->rtpip[profile->rtpip_cur++]);
		tech_pvt->mparams->rtpip = tech_pvt->mparams->rtpip4;
		if (profile->rtpip_cur == profile->rtpip_index) {
			profile->rtpip_cur = 0;
		}
	}

	if (!zstr(profile->rtpip6[profile->rtpip_cur6])) {
		tech_pvt->mparams->rtpip6 = switch_core_session_strdup(tech_pvt->session, profile->rtpip6[profile->rtpip_cur6++]);

		if (zstr(tech_pvt->mparams->rtpip)) {
			tech_pvt->mparams->rtpip = tech_pvt->mparams->rtpip6;
		}

		if (profile->rtpip_cur6 == profile->rtpip_index6) {
			profile->rtpip_cur6 = 0;
		}
	}

	if (zstr(tech_pvt->mparams->rtpip)) {
		switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(tech_pvt->session), SWITCH_LOG_ERROR, "%s has no media ip, check your configuration\n",
			switch_channel_get_name(tech_pvt->channel));
		switch_channel_hangup(tech_pvt->channel, SWITCH_CAUSE_BEARERCAPABILITY_NOTAVAIL);
	}

	tech_pvt->mparams->extrtpip = tech_pvt->mparams->extsipip = profile->extrtpip;

	//tech_pvt->mparams->dtmf_type = tech_pvt->profile->dtmf_type;
	switch_channel_set_flag(tech_pvt->channel, CF_TRACKABLE);
	switch_channel_set_variable(tech_pvt->channel, "secondary_recovery_module", modname);

	switch_core_media_check_dtmf_type(tech_pvt->session);

	//switch_channel_set_cap(tech_pvt->channel, CC_MEDIA_ACK);
	switch_channel_set_cap(tech_pvt->channel, CC_BYPASS_MEDIA);
	//switch_channel_set_cap(tech_pvt->channel, CC_PROXY_MEDIA);
	switch_channel_set_cap(tech_pvt->channel, CC_JITTERBUFFER);
	switch_channel_set_cap(tech_pvt->channel, CC_FS_RTP);

	//switch_channel_set_cap(tech_pvt->channel, CC_QUEUEABLE_DTMF_DELAY);
	//tech_pvt->mparams->ndlb = tech_pvt->profile->mndlb;

	tech_pvt->mparams->inbound_codec_string = switch_core_session_strdup(tech_pvt->session, profile->inbound_codec_string);
	tech_pvt->mparams->outbound_codec_string = switch_core_session_strdup(tech_pvt->session, profile->outbound_codec_string);

	tech_pvt->mparams->jb_msec = "-1";
	switch_media_handle_set_media_flag(tech_pvt->smh, SCMF_SUPPRESS_CNG);

	//tech_pvt->mparams->auto_rtp_bugs = profile->auto_rtp_bugs;
	tech_pvt->mparams->timer_name = profile->timer_name;
	//tech_pvt->mparams->vflags = profile->vflags;
	//tech_pvt->mparams->manual_rtp_bugs = profile->manual_rtp_bugs;
	//tech_pvt->mparams->manual_video_rtp_bugs = profile->manual_video_rtp_bugs;

	tech_pvt->mparams->local_network = switch_core_session_strdup(tech_pvt->session, profile->local_network);


	//tech_pvt->mparams->rtcp_audio_interval_msec = profile->rtpp_audio_interval_msec;
	//tech_pvt->mparams->rtcp_video_interval_msec = profile->rtpp_video_interval_msec;
	//tech_pvt->mparams->sdp_username = profile->sdp_username;
	//tech_pvt->mparams->cng_pt = tech_pvt->cng_pt;
	//tech_pvt->mparams->rtc_timeout_sec = profile->rtp_timeout_sec;
	//tech_pvt->mparams->rtc_hold_timeout_sec = profile->rtp_hold_timeout_sec;
	//switch_media_handle_set_media_flags(tech_pvt->media_handle, tech_pvt->profile->media_flags);


// 	for (i = 0; i < profile->cand_acl_count; i++) {
// 		switch_core_media_add_ice_acl(tech_pvt->session, SWITCH_MEDIA_TYPE_AUDIO, profile->cand_acl[i]);
// 		switch_core_media_add_ice_acl(tech_pvt->session, SWITCH_MEDIA_TYPE_VIDEO, profile->cand_acl[i]);
// 	}
}

switch_status_t MakeCall_FromPdt(uint32_t Mid, uint32_t CalledSSI, uint32_t CallerSSI, uint8_t ChanNum)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	switch_core_session_t *session = NULL;
	pdt_pvt_t *tech_pvt = NULL;
	switch_channel_t *channel = NULL;
	pdt_profile_t *profile = pdt_globals.profile;
	char sCallerSSI[100] = {0};
	char sCallStr[100] = { 0 };
	char channel_name[128] = {0};
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
		"MakeCall_FromPdt!Mid:%d,CalledSSI:%d,CallerSSI:%d,ChanNum:%d\n"
		, Mid, CalledSSI, CallerSSI	, ChanNum);

	switch_snprintf(sCallerSSI, 100, "%d", CallerSSI);
//	switch_snprintf(sCallStr, 100, "group_%s", IM_getAppGroupID(CalledSSI));
	
#ifdef USE_pdtcalllist
	pdt_call_info_t* pGet_call_info = NULL;

	pGet_call_info = Pdt_get_pdtcallinfo_caller(CallerSSI);
	if (pGet_call_info == NULL)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "MakeCall_FromPdt!pdt_get_pdtcallinfo fail \n");		
		return SWITCH_STATUS_FALSE;
	}
	//把新的Mid保存到队列中
	pGet_call_info->Mid = Mid;

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "MakeCall_FromPdt!CallType:%d,Mid:%d\n"
		, pGet_call_info->CallType, pGet_call_info->Mid);
	if (pGet_call_info->CallType == CALL_Type_Multicast)//从缓存中获取呼叫类型
#else
	char* AppUserCode = NULL;
	AppUserCode = IM_getAppUserID_SSI(CalledSSI);
	if (AppUserCode == NULL)


	if (IM_getAppGroupID(CalledSSI))//不是PDT账户号，则为组呼号码
#endif // USE_pdtcalllist
	{
		switch_snprintf(sCallStr, 100, "group_%d", CalledSSI);
		switch_snprintf(channel_name, sizeof(channel_name), "pdt/%d", CalledSSI);
	}
	else {//单呼
		switch_snprintf(sCallStr, 100, "person_%d", CalledSSI);
		switch_snprintf(channel_name, sizeof(channel_name), "pdt/%d", CallerSSI);
	}

	if ((session = switch_core_session_request(pdt_endpoint_interface, SWITCH_CALL_DIRECTION_INBOUND, SOF_NONE, NULL)) != 0) {
		switch_core_session_add_stream(session, NULL);
		if ((tech_pvt = (pdt_pvt_t *)switch_core_session_alloc(session, sizeof(pdt_pvt_t))) != 0) {
			channel = switch_core_session_get_channel(session);
		}
		switch_channel_set_variable(channel, "IsPdt", "true");
		switch_channel_set_variable_printf(channel, "Mid","%d", Mid);
// 		if (AppUserCode != NULL)
// 		{
// 			switch_channel_set_variable(channel, "AppUserCode", AppUserCode);
// 		}

		switch_channel_set_variable_printf(channel, "sip_from_user", "%d", CallerSSI);
		switch_channel_set_variable_printf(channel, "CallerSSI", "%d", CallerSSI);
		switch_channel_set_variable_printf(channel, "CalledSSI", "%d", CalledSSI);
//		switch_channel_set_variable_printf(channel, "Multicast", "%d", CALL_Type_Multicast);
		tech_pvt->session = session;
		tech_pvt->channel = channel;
		tech_pvt->Mid = Mid;
		tech_pvt->CalledSSI = CalledSSI;
		tech_pvt->CallerSSI = CallerSSI;
		tech_pvt->ChanNum = ChanNum;
		if ((tech_pvt->caller_profile = switch_caller_profile_new(switch_core_session_get_pool(session),
			pdt_globals.user_full_id,
			profile->dialplan,
			sCallerSSI,
			sCallerSSI,
			pdt_globals.PdtServerIP,
			NULL,
			NULL,
			NULL, "mod_pdt", profile->context, sCallStr)) != 0) {

			switch_channel_set_name(channel, channel_name);
			switch_channel_set_caller_profile(channel, tech_pvt->caller_profile);

			switch_mutex_init(&tech_pvt->flag_mutex, SWITCH_MUTEX_NESTED, switch_core_session_get_pool(session));
			switch_mutex_init(&tech_pvt->mutex, SWITCH_MUTEX_NESTED, switch_core_session_get_pool(session));
			switch_core_session_set_private(session, tech_pvt);
			switch_core_session_set_private_class(session, tech_pvt, SWITCH_PVT_SECONDARY);

			tech_pvt->call_id = switch_core_session_strdup(session, switch_core_session_get_uuid(session));


			switch_core_media_check_dtmf_type(tech_pvt->session);
			switch_channel_set_cap(tech_pvt->channel, CC_JITTERBUFFER);
			switch_channel_set_cap(tech_pvt->channel, CC_FS_RTP);
			switch_media_handle_create(&tech_pvt->media_handle, tech_pvt->session, &tech_pvt->mediaParams);

			if ((tech_pvt->smh = switch_core_session_get_media_handle(session))) {
				tech_pvt->mparams = switch_core_media_get_mparams(tech_pvt->smh);
			}
			set_media_options(tech_pvt, pdt_globals.profile);

			switch_channel_set_state(channel, CS_INIT);
			status = switch_core_session_thread_launch(session);
			if (status != SWITCH_STATUS_SUCCESS) {
				switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_CRIT, "Error switch_core_session_thread_launch\n");
			}

		}
	}
	return status;
}

static switch_call_cause_t channel_outgoing_channel(switch_core_session_t *session, switch_event_t *var_event,
													switch_caller_profile_t *outbound_profile,
													switch_core_session_t **new_session, switch_memory_pool_t **pool, switch_originate_flag_t flags,
													switch_call_cause_t *cancel_cause)
{
	char name[128];
//	switch_channel_t *ochannel = NULL;

// 	if (session) {
// 		ochannel = switch_core_session_get_channel(session);
// 		switch_channel_clear_flag(ochannel, CF_PROXY_MEDIA);
// 		switch_channel_clear_flag(ochannel, CF_PROXY_MODE);
// 		switch_channel_pre_answer(ochannel);
// 	}

	if ((*new_session = switch_core_session_request(pdt_endpoint_interface, SWITCH_CALL_DIRECTION_OUTBOUND, flags, pool)) != 0) {
		pdt_pvt_t *tech_pvt;
		switch_channel_t *channel;
		switch_channel_t *channel_src;
		switch_caller_profile_t *caller_profile;
		switch_event_t *clone = NULL;

		switch_core_session_add_stream(*new_session, NULL);

		if ((tech_pvt = (pdt_pvt_t *) switch_core_session_alloc(*new_session, sizeof(pdt_pvt_t))) != 0) {
			channel = switch_core_session_get_channel(*new_session);
			channel_src = switch_core_session_get_channel(session);
			tech_pvt->session = *new_session;
			tech_pvt->channel = channel;

			switch_channel_set_variable(channel, "presence_id", pdt_globals.user_full_id);
			switch_channel_set_variable(channel, "chat_proto", PDT_CHAT_PROTO);

			switch_snprintf(name, sizeof(name), "pdt/%s", outbound_profile->destination_number);
			switch_channel_set_name(channel, name);
			switch_mutex_init(&tech_pvt->flag_mutex, SWITCH_MUTEX_NESTED, switch_core_session_get_pool(session));
			switch_mutex_init(&tech_pvt->mutex, SWITCH_MUTEX_NESTED, switch_core_session_get_pool(session));
			switch_core_session_set_private(*new_session, tech_pvt);
			switch_core_session_set_private_class(*new_session, tech_pvt, SWITCH_PVT_SECONDARY);
	
			tech_pvt->call_id = switch_core_session_strdup(*new_session, switch_core_session_get_uuid(*new_session));


			switch_core_media_check_dtmf_type(tech_pvt->session);
			switch_channel_set_cap(tech_pvt->channel, CC_JITTERBUFFER);
			switch_channel_set_cap(tech_pvt->channel, CC_FS_RTP);
			switch_media_handle_create(&tech_pvt->media_handle, tech_pvt->session, &tech_pvt->mediaParams);

			if ((tech_pvt->smh = switch_core_session_get_media_handle(*new_session))) {
				tech_pvt->mparams = switch_core_media_get_mparams(tech_pvt->smh);
			}
			set_media_options(tech_pvt, pdt_globals.profile);
// 			if (tech_init(tech_pvt, *new_session, session ? switch_core_session_get_read_codec(session) : NULL) != SWITCH_STATUS_SUCCESS) {
// 				switch_core_session_destroy(new_session);
// 				return SWITCH_CAUSE_DESTINATION_OUT_OF_ORDER;
// 			}
			if (outbound_profile->destination_number != NULL && strlen(outbound_profile->destination_number) > 0)
			{
				tech_pvt->CalledSSI = atoi(outbound_profile->destination_number);
			}
			if (pdt_globals.UsePdt)
			{
				uint16_t Multicast = 0;
				const char* pdt_muti = switch_channel_get_variable(channel_src, "pdt_multicast");//通过dailplan设置是否组播标记
				if (pdt_muti != NULL && strlen(pdt_muti) != 0)
				{
					Multicast = (uint16_t)atoi(pdt_muti);
					tech_pvt->Multicast = Multicast;
				}

				PDT_API_Makecall(pdt_globals.AgentNo, tech_pvt->CalledSSI, tech_pvt->CallerSSI, 0, Multicast, 300, 0);
			}

		} else {
			switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(*new_session), SWITCH_LOG_CRIT, "Hey where is my memory pool?\n");
			switch_core_session_destroy(new_session);
			return SWITCH_CAUSE_DESTINATION_OUT_OF_ORDER;
		}

		if (switch_event_dup(&clone, var_event) == SWITCH_STATUS_SUCCESS) {
			switch_channel_set_private(channel, "__pdt_vars__", clone);
		}

		if (outbound_profile) {
			char *dialplan = NULL, *context = NULL;

			caller_profile = switch_caller_profile_clone(*new_session, outbound_profile);
			caller_profile->source = switch_core_strdup(caller_profile->pool, modname);
			if (!strncasecmp(caller_profile->destination_number, "app=", 4)) {
				char *dest = switch_core_session_strdup(*new_session, caller_profile->destination_number);
				char *app = dest + 4;
				char *arg = NULL;

				if ((arg = strchr(app, ':'))) {
					*arg++ = '\0';
				}

				switch_channel_set_variable(channel, "pdt_app", app);

				if (clone) {
					switch_event_add_header_string(clone, SWITCH_STACK_BOTTOM, "pdt_app", app);
				}
				
				if (arg) {
					switch_channel_set_variable(channel, "pdt_app_arg", arg);
					if (clone) {
						switch_event_add_header_string(clone, SWITCH_STACK_BOTTOM, "pdt_app_arg", arg);
					}
				}

				switch_set_flag(tech_pvt, TFLAG_APP);

				caller_profile->destination_number = switch_core_strdup(caller_profile->pool, app);
			}

			if ((context = strchr(caller_profile->destination_number, '/'))) {
				*context++ = '\0';

				if ((dialplan = strchr(context, '/'))) {
					*dialplan++ = '\0';
				}

				if (!zstr(context)) {
					caller_profile->context = switch_core_strdup(caller_profile->pool, context);
				}

				if (!zstr(dialplan)) {
					caller_profile->dialplan = switch_core_strdup(caller_profile->pool, dialplan);
				}
			}

			if (zstr(caller_profile->context)) {
				caller_profile->context = switch_core_strdup(caller_profile->pool, "default");
			}

			if (zstr(caller_profile->dialplan)) {
				caller_profile->dialplan = switch_core_strdup(caller_profile->pool, "xml");
			}

//			switch_snprintf(name, sizeof(name), "pdt/%s-a", caller_profile->destination_number);
//			switch_channel_set_name(channel, name);
			switch_set_flag(tech_pvt, TFLAG_OUTBOUND);
			switch_channel_set_caller_profile(channel, caller_profile);
			tech_pvt->caller_profile = caller_profile;
		} else {
			switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(*new_session), SWITCH_LOG_ERROR, "Doh! no caller profile\n");
			switch_core_session_destroy(new_session);
			return SWITCH_CAUSE_DESTINATION_OUT_OF_ORDER;
		}

// 		pdt_globals.cur_session = *new_session;
// 		pdt_globals.cur_channel = channel;
		if (pdt_globals.UsePdt == 0)
		{
			switch_channel_set_state(channel, CS_INIT);
		}
		return SWITCH_CAUSE_SUCCESS;
	}

	return SWITCH_CAUSE_DESTINATION_OUT_OF_ORDER;
}
static switch_status_t read_pdt_data(switch_memory_pool_t *pool,ENUM_PDT_DATA* papiID,void **data, switch_size_t* len)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	PDT_HEAD PdtHead;
	char* pDataBuf = NULL;
	switch_size_t nLenData = 0;
	switch_size_t nHeasSize = sizeof(PDT_HEAD);
	if (data == NULL || pdt_globals.sock == NULL)
	{
		return status;
	}
		
	status = switch_socket_recv(pdt_globals.sock, (char*)&PdtHead, &nHeasSize);
	if (status == SWITCH_STATUS_SUCCESS)
	{
		if (PDT_IsHead(&PdtHead))
		{
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "Get PDT Package!\n");
			nLenData = PdtHead.LEN - nHeasSize;
			pDataBuf = switch_core_alloc(pool, nLenData);
			*len = nLenData;
			status = switch_socket_recv(pdt_globals.sock, pDataBuf, &nLenData);
			*data = pDataBuf;
			if (papiID != NULL)
			{
				*papiID = PDT_GetApiID(PdtHead);
			}
		} else {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Get Error Package!\n");
			status = SWITCH_STATUS_FALSE;
		}
	}

	return status;
}

void parse_pdt_data(ENUM_PDT_DATA apiID, char* pDataBuf, switch_size_t nDataLen)
{
	uint16_t AgentNo = 0;
	memcpy(&AgentNo, pDataBuf, sizeof(AgentNo));
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "Parse PDT Data:%d,nDataLen:%d AgentNo:%d!\n", apiID, nDataLen, AgentNo);
// 	if (pdt_globals.bImConnected == SWITCH_FALSE)
// 	{
// 		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM Server Not Connected Ignore It !\n");
// 		return;
// 	}
	switch (apiID)
	{
	case DATA_ID_LOGIN_LOGOUT_ACK:
	{
		PDTDATA_LoginLogoutAck* pPdtData = (PDTDATA_LoginLogoutAck*)pDataBuf;
		if (pPdtData->Ret == 10)
		{
			pPdtData->Ret = 0;
			pdt_globals.bIsDebugServer = SWITCH_TRUE;
		}
		if (pPdtData->Ret == 0)
		{
			uint8_t VoiceIP[4] = { 0 };
			pdt_globals.bPDTConnected = SWITCH_TRUE;
			memcpy((void*)VoiceIP, (void*)&pPdtData->VoiceIP, 4);
			if (pdt_globals.PdtServerVoicePort == 0)
			{
				pdt_globals.PdtServerVoicePort = pPdtData->VoicePort;
			}

			if (strlen(pdt_globals.PdtServerVoiceIP) == 0)
			{
				sprintf(pdt_globals.PdtServerVoiceIP, "%d.%d.%d.%d", VoiceIP[0], VoiceIP[1], VoiceIP[2], VoiceIP[3]);
			}
			pdt_globals.PdtServerChanSum = pPdtData->ChanSum;
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "[PDTMSG] Login Succ!VoiceIP:%s,port:%d,ChanSum:%d\n", pdt_globals.PdtServerVoiceIP, pPdtData->VoicePort, pPdtData->ChanSum);
		}
	}
	break;
	case DATA_ID_HeartBeatAck:
	{
//		PDTDATA_HeartBeatAck* pPdtData = (PDTDATA_HeartBeatAck*)pDataBuf;
		pdt_globals.timeLastHeartbeat = switch_time_now();
// 		pdt_globals.PdtServerChanSum = pPdtData->Chansum;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "[PDTMSG] HeartBeatAck:!ChanSum:%d\n", pdt_globals.PdtServerChanSum);
	}
	break;
	case DATA_ID_MakecallAck:
	{
		PDTDATA_MakecallAck* pPdtData = (PDTDATA_MakecallAck*)pDataBuf;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] MakecallAck!Mid:%d,CalledSSI:%d,CallerSSI:%d,Opt:%d\n"
			, pPdtData->Mid
			, pPdtData->CalledSSI
			, pPdtData->CallerSSI
			, pPdtData->Opt);
	}
	break;
	case DATA_ID_ServerGetCall://从PDT对讲机呼入时触发
	{
		PDTDATA_ServerGetCall* pPdtData = (PDTDATA_ServerGetCall*)pDataBuf;
		switch_bool_t bAnswerCall = SWITCH_FALSE;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] ServerGetCal!Mid:%d,CalledSSI:%d,CallerSSI:%d,CallType:%d,CallTime:%d,CallGrade:%d\n"
			, pPdtData->Mid
			, pPdtData->CalledSSI
			, pPdtData->CallerSSI
			, pPdtData->CallType
			, pPdtData->CallTime
			, pPdtData->CallGrade);
#ifdef USE_pdtcalllist
		Pdt_add_pdtcallinfo(CALL_From_Pdt, pPdtData->CalledSSI, pPdtData->CallerSSI, pPdtData->CallType, pPdtData->Mid);
#endif // USE_pdtcalllist
		if (pdt_globals.CheckServerGetCall)
		{
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "[PDTMSG] ServerGetCall CheckServerGetCall!\n");

			if (IM_IsPdtSSI(pPdtData->CallerSSI))
			{
				char* cAppCode = NULL;
				if (pPdtData->CallType == 0x00)
				{//单呼
					char sPdtSSI[100] = { 0 };
					switch_snprintf(sPdtSSI, 100, "%d", pPdtData->CalledSSI);

					if (IM_getAppUserID(sPdtSSI,GET_All))
					{
						bAnswerCall = SWITCH_TRUE;
					}
					else {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "[PDTMSG] ServerGetCall Person CalledSSI Not Exist!\n");

					}
				}
				else if (pPdtData->CallType == 0x01)
				{//组呼
					cAppCode = IM_getAppGroupID(pPdtData->CalledSSI);
					if (cAppCode != NULL)
					{
						bAnswerCall = SWITCH_TRUE;
					}
					else {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "[PDTMSG] ServerGetCall Group CalledSSI Not Exist!\n");

					}
				}
			}
			else {
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "[PDTMSG] ServerGetCall CallerSSI Not Exist!\n");
			}

		} 
		else
		{
			bAnswerCall = SWITCH_TRUE;
		}

		if (bAnswerCall)
		{
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "[PDTMSG] ServerGetCall Answer Call!\n");
			PDT_API_MakecallAck(pdt_globals.AgentNo, pPdtData->Mid, pPdtData->CalledSSI, pPdtData->CallerSSI, 0);
		}
		else {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "[PDTMSG] ServerGetCall Unknown Called ID. Ignore!\n");
		}
	}
	break;
	case DATA_ID_ServerSelectedChan:
	{
		PDTDATA_ServerSelectedChan* pPdtData = (PDTDATA_ServerSelectedChan*)pDataBuf;
		switch_status_t status = SWITCH_STATUS_SUCCESS;
// 		pdt_globals.Mid = pPdtData->Mid;
// 		pdt_globals.PdtServerVoiceChanNum = pPdtData->ChanNum;

		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] ServerSelectedChan!Mid:%d,CalledSSI:%d,CallerSSI:%d,ChanNum:%d,FailReason:%d\n"
			, pPdtData->Mid
			, pPdtData->CalledSSI
			, pPdtData->CallerSSI
			, pPdtData->ChanNum
			, pPdtData->FailReason);
		if (pPdtData->FailReason == 0)
		{
			status = MakeCall_FromPdt(pPdtData->Mid, pPdtData->CalledSSI, pPdtData->CallerSSI, pPdtData->ChanNum);
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "[PDTMSG] MakeCall_FromPdt!status:%d\n", status);
		}
		else {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "[PDTMSG] MakeCall_FromPdt!FAIL\n");
			Pdt_del_pdtcallinfo_mid(pPdtData->Mid);
		}
	}
	break;
	case DATA_ID_ServerActionInfo:
	{
		PDTDATA_ServerActionInfo* pPdtData = (PDTDATA_ServerActionInfo*)pDataBuf;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] DATA_ID_ServerActionInfo!Mid:%d,CalledSSI:%d,RadioSSI:%d,RadioType:%d,ActionType:%d,PTTType:%d\n"
			, pPdtData->Mid
			, pPdtData->CalledSSI
			, pPdtData->RadioSSI
			, pPdtData->RadioType
			, pPdtData->ActionType
			, pPdtData->PTTType);
		if (pPdtData->ActionType == 0x03 && pPdtData->PTTType > 0)//PTT消息	pPdtData->PTTType==0为拆线。不在这里处理
		{
// #ifdef USE_pdtcalllist
// 			pdt_call_info_t* call_info = pdt_get_pdtcallinfo(pPdtData->RadioSSI);
// 			if (call_info != NULL && call_info->CallFromType)
// 				//RadioSSI在呼叫列表中找到，并且是PDT发起的呼叫，则是对讲机发起的PTT消息。需要进行转发。
// #else
// 			char* cAppID = IM_getAppUserID_SSI(pPdtData->RadioSSI);
// 			if (cAppID == NULL && pPdtData->RadioSSI != pdt_globals.CallerSSI)
// 				//RadioSSI在IM列表中找不到，则是对讲机发起的PTT消息。需要进行转发。
// #endif // USE_pdtcalllist
			//从SIP用户列表中查询是否有此账户。PDT账户是直接使用PDTSSI做账户名的
			if (IM_IsPdtSSI(pPdtData->RadioSSI))
			{
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Is PDT And send PTT!\n");
				switch_event_t *event;
				char* action = "im-event";
				int nPttState = pPdtData->PTTType == 1 ? PTT_STATE_ON : PTT_STATE_OFF;
// 				uint16_t CallType = CALL_Type_Multicast;
// 				pdt_call_info_t* call_info = Pdt_get_pdtcallinfo_Mid(pPdtData->Mid);
// 				if (call_info != NULL)
// 				{
// 					CallType = call_info->CallType;
// 				}
// 				else {
// 					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "callinfo Not Found! Mid:%d \n", pPdtData->Mid);
// 				}
//				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "CallType:%d\n", CallType);
// 				
// 				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "SendEvent_PTT action:%s\n", action);
// 				if (switch_event_create_subclass(&event, SWITCH_EVENT_CUSTOM, CONF_EVENT_MAINT) == SWITCH_STATUS_SUCCESS) {
// 					switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "EventName", "conference_ptt");
// 					switch_event_add_header(event, SWITCH_STACK_BOTTOM, "ptt_type","%d", nPttState);
// 					switch_event_add_header(event, SWITCH_STACK_BOTTOM, "Multicast", "%d", CallType);
// 					switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "IsPdt", "1");
// 					switch_event_add_header(event, SWITCH_STACK_BOTTOM, "CallerSSI","%d", pPdtData->RadioSSI);
// 					switch_event_add_header(event, SWITCH_STACK_BOTTOM, "CalledSSI","%d", pPdtData->CalledSSI);
// 					switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "Action", action);
// 					//event_handler_IM_event(event);
// 					switch_event_fire(&event);
// 				}
				Pdt_SendEvent_PTT(pPdtData->Mid, nPttState, pPdtData->RadioSSI, pPdtData->CalledSSI);
			}
			else {
				//非空，则为APP发起的PTT动作
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "APP send PTT!\n");
			}

		}
	}
	break;	
	case DATA_ID_Quit:
	{
		PDTDATA_Quit* pPdtData = (PDTDATA_Quit*)pDataBuf;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] Quit!Mid:%d,ReqFrom:%d,ChanNum:%d,Opt:%d\n"
			, pPdtData->Mid
			, pPdtData->ReqFrom
			, pPdtData->ChanNum
			, pPdtData->Opt);
		Pdt_SendEvent_Quit(pPdtData->Mid, pPdtData->ChanNum, pPdtData->ReqFrom,pPdtData->Opt,0);
		Pdt_del_pdtcallinfo_mid(pPdtData->Mid);
// 		if (pdt_globals.cur_channel != NULL)
// 		{
// 			switch_channel_hangup(pdt_globals.cur_channel, SWITCH_CAUSE_NORMAL_CLEARING);
// 			pdt_globals.cur_channel = NULL;
// 			pdt_globals.cur_session = NULL;
// 		}
	}
	break;
	case DATA_ID_SMS:
	{
		PDTDATA_SMS* pPdtData = (PDTDATA_SMS*)pDataBuf;
		uint8_t Ret = 0;
		char* AppGroupID = NULL;
		char SenderUserCode[100] = { 0 };
		const char* cGroupType = "";
		char* ContentOri = (char*)pPdtData + sizeof(PDTDATA_SMS);
		int ContentOriLen = nDataLen - sizeof(PDTDATA_SMS);
//		int OutputLeftLen = 0;
		char Content[2048] = { 0 };
//		int OutputLen = sizeof(Content);
		memset(Content, '\0', sizeof(Content));
		memcpy(Content, ContentOri, ContentOriLen);
//		OutputLeftLen = trans("UTF-16", "GBK", ContentOri, ContentOriLen, Content, OutputLen);

		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] SMS!CalledSSI:%d,CallerSSI:%d,Ctype:%d,SMS:%s\n"
			, pPdtData->CalledSSI
			, pPdtData->CallerSSI
			, pPdtData->Ctype
			, Content);

		//应答结果，单呼时：=0 发送成功，=1发送失败；组呼：=1发送成功。
		if (pPdtData->Ctype == 0x00){//单呼
			Ret = 0;
			cGroupType = "Person";
			AppGroupID = IM_getAppUserID_SSI(pPdtData->CalledSSI,GET_All);
		}else if (pPdtData->Ctype == 0x01) {
		//组呼
			cGroupType = "Group";
			AppGroupID = IM_getAppGroupID(pPdtData->CalledSSI);
			Ret = 1;
		}
		switch_snprintf(SenderUserCode, 100, "%d", pPdtData->CallerSSI);
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "[PDTMSG] SenderUserCode:%s\n", SenderUserCode);
		IM_pushMsg(cGroupType, AppGroupID, Content, NULL, SenderUserCode);

		PDT_API_SMS_Ack(pdt_globals.AgentNo, pPdtData->CalledSSI, pPdtData->CallerSSI, pPdtData->Ctype, Ret);
	}
	break;
	case DATA_ID_SMS_Ack:
	{
		PDTDATA_SMS_Ack* pPdtData = (PDTDATA_SMS_Ack*)pDataBuf;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] SMS_Ack!CalledSSI:%d,CallerSSI:%d,Ctype:%d,Ret:%d\n"
			, pPdtData->CalledSSI
			, pPdtData->CallerSSI
			, pPdtData->Ctype
			, pPdtData->Ret);


	}
	break;
	case DATA_ID_MANAGE_Ack:
	{
		PDTDATA_MANAGE_Ack* pPdtData = (PDTDATA_MANAGE_Ack*)pDataBuf;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
			"[PDTMSG] MANAGE_Ack!RadioSSI:%d,Opt:%d,Ret:%d\n"
			, pPdtData->RadioSSI
			, pPdtData->Opt
			, pPdtData->Ret);
		if (pdt_globals.cond_api_manage != NULL)
		{
			pdt_globals.Ret_api_manage = pPdtData->Ret;
			switch_thread_cond_signal(pdt_globals.cond_api_manage);
		}
	}

	}
}
static void *SWITCH_THREAD_FUNC listener_run(switch_thread_t *thread, void *obj)
{
	char* pDataBuf = NULL;
	switch_size_t nDataLen;
	switch_status_t status;
//	char reply[512] = "";
//	switch_core_session_t *session = NULL;
// 	switch_channel_t *channel = NULL;
// 	switch_event_t *revent = NULL;
// 	const char *var;
// 	int locked = 1;
	switch_memory_pool_t *pool;
	switch_core_new_memory_pool(&pool);
	while (pdt_globals.running) {
		ENUM_PDT_DATA apiID;
		status = read_pdt_data(pool,&apiID,&pDataBuf,&nDataLen);
		if (status == SWITCH_STATUS_SUCCESS && pDataBuf != NULL && nDataLen > 0)
		{
			parse_pdt_data(apiID, pDataBuf, nDataLen);
		} else {
			switch_sleep(1000000);
		}
		
	}
	return NULL;

}

/* Create a thread for the socket and launch it */
static void launch_listener_thread()
{
	if (pdt_globals.threadRecv == NULL)
	{
		switch_threadattr_t *thd_attr = NULL;

		switch_threadattr_create(&thd_attr, pdt_globals.pool);
		switch_threadattr_detach_set(thd_attr, 1);
		switch_threadattr_stacksize_set(thd_attr, SWITCH_THREAD_STACKSIZE);
		switch_thread_create(&pdt_globals.threadRecv, thd_attr, listener_run, NULL, pdt_globals.pool);
	}
}
void *SWITCH_THREAD_FUNC pdt_thread_connect(switch_thread_t *thread, void *obj)
{
	switch_socket_t *new_sock = NULL;
	switch_sockaddr_t *sa;
	switch_status_t connected = SWITCH_STATUS_FALSE;
	int nErrCountFor_IM_login = 0;
	int nErrCountFor_PDT_HB = 0;
	int nCountFor_IM_loginToken = 0;
	int nLastImStatusCheckTime_ms = 0;
	while (pdt_globals.running) {
		if (new_sock == NULL)
		{
			pdt_globals.sock = NULL;
			if (switch_sockaddr_info_get(&sa, pdt_globals.PdtServerIP, SWITCH_UNSPEC, pdt_globals.PdtServerPort, 0, pdt_globals.pool) != SWITCH_STATUS_SUCCESS) {
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Socket Error!\n");
				continue;
			}
			if (switch_socket_create(&new_sock, switch_sockaddr_get_family(sa), SOCK_STREAM, SWITCH_PROTO_TCP, pdt_globals.pool)
				!= SWITCH_STATUS_SUCCESS) {
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Socket Error!\n");
				continue;
			}

			switch_socket_opt_set(new_sock, SWITCH_SO_KEEPALIVE, 1);
			switch_socket_opt_set(new_sock, SWITCH_SO_TCP_NODELAY, 1);
			switch_socket_opt_set(new_sock, SWITCH_SO_TCP_KEEPIDLE, 30);
			switch_socket_opt_set(new_sock, SWITCH_SO_TCP_KEEPINTVL, 30);
			if ((connected = switch_socket_connect(new_sock, sa)) != SWITCH_STATUS_SUCCESS) {
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "PDT Server Connect Error!wait for next try!\n");
				switch_socket_close(new_sock);
				switch_sleep(10000000);
				new_sock = NULL;
				continue;
			}
			switch_sleep(1000000);
			pdt_globals.sock = new_sock;
			PDT_API_Login(pdt_globals.AgentNo);
			launch_listener_thread();
// 			
// 			switch_sleep(1000000);
// //			PDT_API_Makecall(pdt_globals.AgentNo, 80020261, pdt_globals.CallerSSI, 0, 0, 300, 0);
// 			PDT_API_Makecall(pdt_globals.AgentNo, 80098900, pdt_globals.CallerSSI,0, 1, 300, 0);
// 			switch_sleep(300000);
// 			PDT_API_PTT(pdt_globals.AgentNo,pdt_globals.Mid, pdt_globals.CallerSSI, 1);

//  			switch_sleep(300000);
// 			char cSms[] = { "中文" };
//  			PDT_API_SMS(pdt_globals.AgentNo, 80020261,pdt_globals.CallerSSI,0, cSms,strlen(cSms) + 1);
		}
		//登录IM服务器
		nCountFor_IM_loginToken++;
		if (nCountFor_IM_loginToken > 100)
		{		
			//重连IM重新获取IM Token
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "ReLogin IM Server To Refresh Token!!!!\n");
			pdt_globals.im_ApiToken = NULL;
			nErrCountFor_IM_login = 0;
			nCountFor_IM_loginToken = 0;
		}
		if (pdt_globals.im_ApiToken == NULL || strlen(pdt_globals.im_ApiToken) == 0)
		{//nErrCountFor错误次数太多。不进行login
			if (nErrCountFor_IM_login == 0 || nErrCountFor_IM_login > 30)
			{
				IM_Login();
				nErrCountFor_IM_login = 1;
			}
			nErrCountFor_IM_login++;
 		}
		if (!zstr(pdt_globals.im_ApiToken))
		{//Token获取成功，需要检查状态
			int nNowTime_ms = switch_time_now() / 1000;
			if (nNowTime_ms - nLastImStatusCheckTime_ms > pdt_globals.IMStatusCheckInterval * 1000)
			{
				switch_status_t imStatus = IM_getIMStatus();
				nLastImStatusCheckTime_ms = switch_time_now() / 1000;
				if (imStatus == SWITCH_STATUS_SUCCESS)
				{
					pdt_globals.bImConnected = SWITCH_TRUE;
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM Server Check OK!\n");
				}
				else
				{
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM Server Check FAIL!\n");
					pdt_globals.bImConnected = SWITCH_FALSE;
				}
			}
		}


		if (PDT_API_Heartbeat(pdt_globals.AgentNo) != SWITCH_STATUS_SUCCESS)
		{
			nErrCountFor_PDT_HB++;
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "PDT HeartBeat Fail nErrCountFor_PDT_HB:%d\n", nErrCountFor_PDT_HB);
			if (nErrCountFor_PDT_HB > 3)
			{
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "PDT HeartBeat Fail  socket_close\n");
				switch_socket_close(new_sock);
				pdt_globals.sock = NULL;
				new_sock = NULL;
			}
		}
		else {
			nErrCountFor_PDT_HB = 0;
		}

 		switch_sleep(2000000);

	}




	if (pdt_globals.sock != NULL)
	{
		switch_socket_close(pdt_globals.sock);
		pdt_globals.sock = NULL;
	}
	return NULL;
}
/* Create a thread for the pdt and launch it */
void pdt_launch_thread()
{
	switch_thread_t *thread;
	switch_threadattr_t *thd_attr = NULL;

	switch_threadattr_create(&thd_attr, pdt_globals.pool);
	switch_threadattr_detach_set(thd_attr, 1);
	switch_threadattr_priority_set(thd_attr, SWITCH_PRI_REALTIME);
	switch_threadattr_stacksize_set(thd_attr, SWITCH_THREAD_STACKSIZE);
	switch_thread_create(&thread, thd_attr, pdt_thread_connect, NULL, pdt_globals.pool);
	pdt_globals.threadConnect = thread;
}

static void parse_ip(char *host, switch_size_t host_len, uint16_t *port, char *input)
{
	char *p;
	//struct hostent *hent;
	if ((p = strchr(input, '['))) {
		char *end = switch_find_end_paren(p, '[', ']');
		if (end) {
			p++;
			strncpy(host, p, end - p);
			if (*(end + 1) == ':' && end + 2 < end_of_p(input)) {
				end += 2;
				if (end) {
					*port = (uint16_t)atoi(end);
				}
			}
		}
		else {
			strncpy(host, "::", host_len);
		}
	}
	else {
		strncpy(host, input, host_len);
		if ((p = strrchr(host, ':')) != NULL) {
			*p++ = '\0';
			*port = (uint16_t)atoi(p);
		}
	}

#if 0
	if (host[0] < '0' || host[0] > '9') {
		// Non-numeric host (at least it doesn't start with one).  Convert it to ip addr first
		if ((hent = gethostbyname(host)) != NULL) {
			if (hent->h_addrtype == AF_INET) {
				memcpy(addr, hent->h_addr_list[0], 4);
			}
		}

	}
	else {
		*addr = inet_addr(host);
	}
#endif
}

static switch_status_t parse_config(const char *cf)
{

	switch_xml_t cfg, xml, settings, param, xprofile, xprofiles;
	//switch_xml_t xvhosts, xvhost, rewrites, rule;
	switch_status_t status = SWITCH_STATUS_SUCCESS;

	if (!(xml = switch_xml_open_cfg(cf, &cfg, NULL))) {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Open of %s failed\n", cf);
		return SWITCH_STATUS_TERM;
	}

	if ((xprofiles = switch_xml_child(cfg, "profiles"))) {
		for (xprofile = switch_xml_child(xprofiles, "profile"); xprofile; xprofile = xprofile->next) {
			pdt_profile_t *profile;
			switch_memory_pool_t *pool;
			const char *name = switch_xml_attr(xprofile, "name");

			if (zstr(name)) {
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Required field name missing\n");
				continue;
			}

// 			if (profile_exists(name)) {
// 				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Profile %s already exists\n", name);
// 				continue;
// 			}


			switch_core_new_memory_pool(&pool);
			profile = switch_core_alloc(pool, sizeof(*profile));
			profile->pool = pool;
			profile->name = switch_core_strdup(profile->pool, name);
			switch_mutex_init(&profile->mutex, SWITCH_MUTEX_NESTED, profile->pool);
			switch_thread_rwlock_create(&profile->rwlock, profile->pool);
			//add_profile(profile);
			pdt_globals.profile = profile;

			profile->local_network = "localnet.auto";


			for (param = switch_xml_child(xprofile, "param"); param; param = param->next) {
				char *var = NULL;
				char *val = NULL;
				int i = 0;

				var = (char *)switch_xml_attr_soft(param, "name");
				val = (char *)switch_xml_attr_soft(param, "value");

				if (!strcasecmp(var, "bind-local")) {
					const char *secure = switch_xml_attr_soft(param, "secure");
					if (i < MAX_BIND) {
						parse_ip(profile->ip[profile->i].local_ip, sizeof(profile->ip[profile->i].local_ip), &profile->ip[profile->i].local_port, val);
						if (switch_true(secure)) {
							profile->ip[profile->i].secure = 1;
						}
						profile->i++;
					}
					else {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Max Bindings Reached!\n");
					}
				}
				else if (!strcasecmp(var, "inbound-codec-string") && !zstr(val)) {
					profile->inbound_codec_string = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "outbound-codec-string") && !zstr(val)) {
					profile->outbound_codec_string = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "blind-reg") && !zstr(val)) {
					profile->blind_reg = switch_true(val);
				}
				else if (!strcasecmp(var, "userauth") && !zstr(val)) {
					profile->userauth = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "root-password") && !zstr(val)) {
					profile->root_passwd = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "context") && !zstr(val)) {
					profile->context = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "dialplan") && !zstr(val)) {
					profile->dialplan = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "mcast-ip") && val) {
					profile->mcast_ip = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "mcast-port") && val) {
					profile->mcast_port = (switch_port_t)atoi(val);
				}
				else if (!strcasecmp(var, "timer-name") && !zstr(var)) {
					profile->timer_name = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "force-register-domain") && !zstr(val)) {
					profile->register_domain = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "local-network") && !zstr(val)) {
					profile->local_network = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "apply-candidate-acl")) {
					if (profile->cand_acl_count < SWITCH_MAX_CAND_ACL) {
						profile->cand_acl[profile->cand_acl_count++] = switch_core_strdup(profile->pool, val);
					}
					else {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Max acl records of %d reached\n", SWITCH_MAX_CAND_ACL);
					}
				}
				else if (!strcasecmp(var, "apply-connection-acl")) {
					if (profile->conn_acl_count < SWITCH_MAX_CAND_ACL) {
						profile->conn_acl[profile->conn_acl_count++] = switch_core_strdup(profile->pool, val);
					}
					else {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Max acl records of %d reached\n", SWITCH_MAX_CAND_ACL);
					}
				}
				else if (!strcasecmp(var, "rtp-ip")) {
					if (zstr(val)) {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_WARNING, "Invalid RTP IP.\n");
					}
					else {
						if (strchr(val, ':')) {
							if (profile->rtpip_index6 < MAX_RTPIP - 1) {
								profile->rtpip6[profile->rtpip_index6++] = switch_core_strdup(profile->pool, val);
							}
							else {
								switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_WARNING, "Too many RTP IP.\n");
							}
						}
						else {
							if (profile->rtpip_index < MAX_RTPIP - 1) {
								profile->rtpip[profile->rtpip_index++] = switch_core_strdup(profile->pool, val);
							}
							else {
								switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_WARNING, "Too many RTP IP.\n");
							}
						}
					}
				}
				else if (!strcasecmp(var, "ext-rtp-ip")) {
					if (zstr(val)) {
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_WARNING, "Invalid External RTP IP.\n");
					}
					else {
						profile->extrtpip = switch_core_strdup(profile->pool, val);
					}
				}
				else if (!strcasecmp(var, "debug")) {
					if (val) {
						profile->debug = atoi(val);
					}
				}
				else if (!strcasecmp(var, "record-template")) {
					profile->record_template = switch_core_strdup(profile->pool, val);
				}
				else if (!strcasecmp(var, "record-path")) {
					profile->record_path = switch_core_strdup(profile->pool, val);
				}
			}

			if (zstr(profile->outbound_codec_string)) {
				profile->outbound_codec_string = "PCMA,vp8";
			}

			if (zstr(profile->inbound_codec_string)) {
				profile->inbound_codec_string = profile->outbound_codec_string;
			}

			if (zstr(profile->timer_name)) {
				profile->timer_name = "soft";
			}

			if (zstr(profile->dialplan)) {
				profile->dialplan = "XML";
			}

			if (zstr(profile->context)) {
				profile->context = "default";
			}

			if (zstr(profile->ip[0].local_ip)) switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "%s: local_ip bad\n", profile->name);
			if (profile->ip[0].local_port <= 0) switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "%s: local_port bad\n", profile->name);

			if (zstr(profile->ip[0].local_ip) || profile->ip[0].local_port <= 0) {
				//del_profile(profile);
				pdt_globals.profile = NULL;
				switch_core_destroy_memory_pool(&pool);
			}
			else {
				int i;

				for (i = 0; i < profile->i; i++) {
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG,
						strchr(profile->ip[i].local_ip, ':') ? "%s Bound to [%s]:%d\n" : "%s Bound to %s:%d\n",
						profile->name, profile->ip[i].local_ip, profile->ip[i].local_port);
				}
			}

		} // xprofile
	} // xprofiles

	if ((settings = switch_xml_child(cfg, "settings"))) {
		for (param = switch_xml_child(settings, "param"); param; param = param->next) {
			char *var = NULL;
			char *val = NULL;

			var = (char *)switch_xml_attr_soft(param, "name");
			val = (char *)switch_xml_attr_soft(param, "value");


			if (!strcasecmp(var, "debug")) {
				if (val) {
					pdt_globals.debug = atoi(val);
				}
			}
			else if (!strcasecmp(var, "PdtServerIP") && val) {
				if (strlen(val) > 0) {
					strcpy(pdt_globals.PdtServerIP, val);
				}
			}
			else if (!strcasecmp(var, "PdtServerPort") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.PdtServerPort = (uint16_t)tmp;
				}
			}
			else if (!strcasecmp(var, "AgentNo") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.AgentNo = (uint16_t)tmp;
				}
			}
			else if (!strcasecmp(var, "CallerSSI") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.CallerSSI = tmp;
				}
			}
			else if (!strcasecmp(var, "UsePdt") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.UsePdt = tmp;
				}
			}
			else if (!strcasecmp(var, "UserID") && val) {
				if (strlen(val) > 0) {
					char cUserID[512] = { 0 };
					switch_snprintf(cUserID, sizeof(cUserID), "%s@%s", val, pdt_globals.domain_name);
					pdt_globals.user_full_id = switch_core_strdup(pdt_globals.pool, cUserID);
				}
			}
			else if (!strcasecmp(var, "PdtServerVoiceIP") && val) {
				if (strlen(val) > 0) {
					strcpy(pdt_globals.PdtServerVoiceIP, val);
				}
			}
			else if (!strcasecmp(var, "PdtServerVoicePort") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.PdtServerVoicePort = (uint16_t)tmp;
				}
			}
			else if (!strcasecmp(var, "PdtServerVoiceChanNum") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.PdtServerVoiceChanNum = (uint16_t)tmp;
				}
			}
			else if (!strcasecmp(var, "im_LoginName")) {
				pdt_globals.im_LoginName = switch_core_strdup(pdt_globals.pool, val);
			}
			else if (!strcasecmp(var, "im_UserPwd")) {
				pdt_globals.im_UserPwd = switch_core_strdup(pdt_globals.pool, val);
			}
			else if (!strcasecmp(var, "im_ServerUrl")) {
				pdt_globals.im_ServerUrl = switch_core_strdup(pdt_globals.pool, val);
			}
			else if (!strcasecmp(var, "CheckServerGetCall") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.CheckServerGetCall = tmp;
				}
			}
			else if (!strcasecmp(var, "threadEventListInterval") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.threadEventListInterval = tmp*1000;
				}
			}
			else if (!strcasecmp(var, "threadEventListRetryCount") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.threadEventListRetryCount = tmp;
				}
			}
			else if (!strcasecmp(var, "IMStatusCheckInterval") && val) {
				int tmp = atoi(val);
				if (tmp > 0) {
					pdt_globals.IMStatusCheckInterval = tmp;
				}
			}
		}
	}

	switch_xml_free(xml);

	return status;
}
static int pdt_send_chat(const char *uid, const char *call_id, char *msg)
{
	int done = 0;

	if (call_id) {
		switch_core_session_t *session;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "pdt_send_chat,call_id:%s\n", call_id);
		if ((session = switch_core_session_locate(call_id))) {
			pdt_pvt_t *tech_pvt = switch_core_session_get_private_class(session, SWITCH_PVT_SECONDARY);
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "pdt_send_chat,tech_pvt:%p\n", (void*)tech_pvt);
			if (PDT_API_SMS(pdt_globals.AgentNo, tech_pvt->CalledSSI, tech_pvt->CallerSSI, tech_pvt->Multicast, msg,(uint16_t)strlen(msg) + 1) == SWITCH_STATUS_SUCCESS) {
				done = 1;
			}
			switch_core_session_rwunlock(session);
		}
		else {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "pdt_send_chat switch_core_session_locate fail\n");
		}
	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "pdt_send_chat call_id is null\n");

	}
	return done;
}

static switch_status_t chat_send(switch_event_t *message_event)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	const char *to = switch_event_get_header(message_event, "to");
	const char *from = switch_event_get_header(message_event, "from");
	char *body = switch_event_get_body(message_event);
	const char *call_id = switch_event_get_header(message_event, "call_id");

	//DUMP_EVENT(message_event);
	if (!zstr(to) && !zstr(body) && !zstr(from)) {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "chat_send,from:%s,to:%s,call_id:%s,body:%s\n", from,to, switch_str_nil(call_id), body);
		if (pdt_send_chat(to, call_id, body) == 1){
			status = SWITCH_STATUS_SUCCESS;
		}
	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "INVALID EVENT\n");
		DUMP_EVENT(message_event);
		status = SWITCH_STATUS_FALSE;
	}
	return status;
}

pdt_call_info_t *Pdt_add_pdtcallinfo(uint32_t CallFromType, uint32_t CalledSSI, uint32_t CallerSSI, uint32_t CallType, uint32_t Mid)
{
	pdt_call_info_t *rel = NULL;
	switch_bool_t bAdd = SWITCH_FALSE;

	if (Mid != 0)
	{
		rel = Pdt_get_pdtcallinfo_Mid(Mid);
	}
	else
	{
		rel = Pdt_get_pdtcallinfo_caller(CallerSSI);

	}
	if (rel == NULL)
	{
		bAdd = SWITCH_TRUE;
		rel = switch_core_alloc(pdt_globals.pool, sizeof(*rel));
		//switch_malloc(rel,sizeof(*rel));
		//switch_safe_free();
	}
	if (CallerSSI == 0 || rel== NULL)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_pdtcallinfo NULL\n");
		return NULL;
	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_pdtcallinfo CalledSSI:%d,CallerSSI:%d,Mid:%d,CallType:%d,CallFromType:%d\n",
		CalledSSI, CallerSSI, Mid, CallType, CallFromType);
	switch_mutex_lock(pdt_globals.mutex_get_call_info);
	rel->CalledSSI = CalledSSI;
	rel->CallerSSI = CallerSSI;
	rel->CallType = CallType;
	rel->CallFromType = CallFromType;
	rel->Mid = Mid;

	if (bAdd)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_pdtcallinfo New Add! CallerSSI:%d,Mid:%d\n", CallerSSI, Mid);
		rel->next = pdt_globals.get_call_infos;
		pdt_globals.get_call_infos = rel;
	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_pdtcallinfo Exist! CallerSSI:%d,Mid:%d\n", CallerSSI, Mid);

	}
	switch_mutex_unlock(pdt_globals.mutex_get_call_info);

	return rel;
}
switch_status_t Pdt_del_pdtcallinfo_mid(uint32_t Mid)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	pdt_call_info_t *rel, *last = NULL;

	if (Mid == 0)
		return status;

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt_del_getcallinfo_mid Mid:%d\n", Mid);
	switch_mutex_lock(pdt_globals.mutex_get_call_info);
	for (rel = pdt_globals.get_call_infos; rel; rel = rel->next) {
		if (rel->Mid == Mid) {
			status = SWITCH_STATUS_SUCCESS;
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt_del_getcallinfo_mid Delete One\n");

			if (last) {
				last->next = rel->next;
			}
			else {
				pdt_globals.get_call_infos = rel->next;
			}

			continue;
		}

		last = rel;
	}
	switch_mutex_unlock(pdt_globals.mutex_get_call_info);

	return status;
}
pdt_call_info_t *Pdt_get_pdtcallinfo_caller(uint32_t CallerSSI)
{
	pdt_call_info_t *rel = NULL;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_get_pdtcallinfo_caller CalledSSI:%d\n", CallerSSI);
	if (CallerSSI == 0)
		return NULL;

	switch_mutex_lock(pdt_globals.mutex_get_call_info);
	for (rel = pdt_globals.get_call_infos; rel; rel = rel->next) {
		if (rel->CallerSSI == CallerSSI) {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_get_pdtcallinfo_caller GET CalledSSI:%d,CallerSSI:%d,Mid:%d,CallType:%d,CallFromType:%d\n", 
				rel->CalledSSI, rel->CallerSSI, rel->Mid, rel->CallType, rel->CallFromType);
			break;
		}
	}
	switch_mutex_unlock(pdt_globals.mutex_get_call_info);
	return rel;
}
pdt_call_info_t *Pdt_get_pdtcallinfo_Mid(uint32_t Mid)
{
	pdt_call_info_t *rel = NULL;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_get_pdtcallinfo_Mid Mid:%d\n", Mid);
	if (Mid == 0)
		return NULL;

	switch_mutex_lock(pdt_globals.mutex_get_call_info);
	for (rel = pdt_globals.get_call_infos; rel; rel = rel->next) {
		if (rel->Mid == Mid) {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_get_pdtcallinfo_Mid GET CalledSSI:%d,CallerSSI:%d,Mid:%d,CallType:%d,CallFromType:%d\n",
				rel->CalledSSI, rel->CallerSSI, rel->Mid, rel->CallType, rel->CallFromType);
			break;
		}
	}
	switch_mutex_unlock(pdt_globals.mutex_get_call_info);
	return rel;
}
SWITCH_STANDARD_API(pdt_function)
{
	return pdt_cmd_main(cmd, session, stream);
}


static switch_state_handler_table_t channel_event_handlers = {
	/*.on_init */ channel_on_init,
	/*.on_routing */ channel_on_routing,
	/*.on_execute */ channel_on_execute,
	/*.on_hangup */ channel_on_hangup,
	/*.on_exchange_media */ channel_on_exchange_media,
	/*.on_soft_execute */ channel_on_soft_execute,
	/*.on_consume_media */ channel_on_consume_media,
	/*.on_hibernate */ channel_on_hibernate,
	/*.on_reset */ channel_on_reset,
	/*.on_park */ NULL,
	/*.on_reporting */ NULL,
	/*.on_destroy */ channel_on_destroy
};

static switch_io_routines_t channel_io_routines = {
	/*.outgoing_channel */ channel_outgoing_channel,
	/*.read_frame */ channel_read_frame,
	/*.write_frame */ channel_write_frame,
	/*.kill_channel */ channel_kill_channel,
	/*.send_dtmf */ channel_send_dtmf,
	/*.receive_message */ channel_receive_message
};

SWITCH_STANDARD_APP(unloop_function) { /* NOOP */}

SWITCH_MODULE_LOAD_FUNCTION(mod_pdt_load)
{
	switch_application_interface_t *app_interface = NULL;
	switch_api_interface_t *api_interface = NULL;
	switch_chat_interface_t *chat_interface = NULL;
	if (switch_event_reserve_subclass("pdt::bowout") != SWITCH_STATUS_SUCCESS) {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Couldn't register subclass %s!\n", "pdt::bowout");
		return SWITCH_STATUS_TERM;
	}

	
	memset(&pdt_globals, 0, sizeof(pdt_globals));
	pdt_globals.pool = pool;

	/* connect my internal structure to the blank pointer passed to me */
	*module_interface = switch_loadable_module_create_module_interface(pool, modname);
	pdt_endpoint_interface = switch_loadable_module_create_interface(*module_interface, SWITCH_ENDPOINT_INTERFACE);
	pdt_endpoint_interface->interface_name = "pdt";
	pdt_endpoint_interface->io_routines = &channel_io_routines;
	pdt_endpoint_interface->state_handler = &channel_event_handlers;

	SWITCH_ADD_APP(app_interface, "unloop", "Tell pdt to unfold", "Tell pdt to unfold", unloop_function, "", SAF_NO_LOOPBACK);
	SWITCH_ADD_API(api_interface, "pdt", "PDT API", pdt_function, "syntax");

	SWITCH_ADD_CHAT(chat_interface, PDT_CHAT_PROTO, chat_send);


	if (switch_event_bind(modname, SWITCH_EVENT_CUSTOM, SWITCH_EVENT_SUBCLASS_ANY, Pdt_event_handler, NULL) != SWITCH_STATUS_SUCCESS) {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Couldn't bind SWITCH_EVENT_CUSTOM!\n");
		return SWITCH_STATUS_GENERR;
	}
	if (switch_event_bind(modname, SWITCH_EVENT_RECORD_STOP, SWITCH_EVENT_SUBCLASS_ANY, Pdt_event_record_stop, NULL) != SWITCH_STATUS_SUCCESS) {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Couldn't bind SWITCH_EVENT_RECORD_STOP!\n");
		return SWITCH_STATUS_GENERR;
	}
	//init
	switch_mutex_init(&pdt_globals.mutex_get_call_info, SWITCH_MUTEX_NESTED, pdt_globals.pool);
	switch_mutex_init(&pdt_globals.mutex_api_manage, SWITCH_MUTEX_UNNESTED, pool);
	switch_mutex_init(&pdt_globals.mutex_event_list, SWITCH_MUTEX_NESTED, pdt_globals.pool);
	switch_mutex_init(&pdt_globals.mutex_thread_event_list, SWITCH_MUTEX_UNNESTED, pdt_globals.pool);
	switch_thread_cond_create(&pdt_globals.cond_api_manage, pool);
	switch_thread_cond_create(&pdt_globals.cond_thread_event_list, pool);

	pdt_globals.domain_name = switch_core_get_domain(SWITCH_TRUE);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "domain_name:%s\n", pdt_globals.domain_name);

	//设置默认值
	pdt_globals.threadEventListInterval = (1000 * 1000);
	pdt_globals.threadEventListRetryCount = 5;
	pdt_globals.IMStatusCheckInterval = 10;


	parse_config("pdt.conf");

// 	strcpy(pdt_globals.PdtServerIP, "192.168.191.129");
// 	pdt_globals.PdtServerPort = 9118;
// 	pdt_globals.AgentNo = 101;
// 	pdt_globals.CallerSSI = 80020888;


	pdt_globals.running = 1;
// 	if (pdt_globals.UsePdt == 1)
// 	{//需要使用连接PDT服务器
		pdt_launch_thread();
		pdt_launch_thread_Eventlist();
//	}
	

	/* indicate that the module should continue to be loaded */
	return SWITCH_STATUS_SUCCESS;
}

SWITCH_MODULE_SHUTDOWN_FUNCTION(mod_pdt_shutdown)
{

	switch_event_free_subclass("pdt::bowout");
	
	return SWITCH_STATUS_SUCCESS;
}

/* For Emacs:
 * Local Variables:
 * mode:c
 * indent-tabs-mode:t
 * tab-width:4
 * c-basic-offset:4
 * End:
 * For VIM:
 * vim:set softtabstop=4 shiftwidth=4 tabstop=4 noet:
 */



