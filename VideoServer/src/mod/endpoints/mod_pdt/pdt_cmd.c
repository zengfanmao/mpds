#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "mod_pdt.h"
#include "pdt_api.h"
#include "im_api.h"
extern globals_t pdt_globals;


typedef switch_status_t(*pdt_command_t) (char **argv, int argc, switch_stream_handle_t *stream);
static switch_status_t cmd_pdt_login(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	status = PDT_API_Login(AgentNo);
	stream->write_function(stream, "login AgentNo:%d,status:%d\n", AgentNo, status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_Heartbeat(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	stream->write_function(stream, "Heartbeat AgentNo:%d,status:%d\n", AgentNo, status);
	status = PDT_API_Heartbeat(AgentNo);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_Makecall(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	uint32_t CalledSSI = (uint32_t)switch_safe_atoi(argv[1], 0);
	uint32_t CallerSSI = (uint32_t)switch_safe_atoi(argv[2], 0);
	uint8_t IsSimulate = (uint8_t)switch_safe_atoi(argv[3], 0);
	uint16_t CallType = (uint16_t)switch_safe_atoi(argv[4], 0);
	uint16_t CallTime = (uint16_t)switch_safe_atoi(argv[5], 0);
	uint8_t Conv = (uint8_t)switch_safe_atoi(argv[6], 0);
	status = PDT_API_Makecall(AgentNo,CalledSSI,CallerSSI, IsSimulate,CallType,CallTime,Conv);
	stream->write_function(stream, "Makecall AgentNo:%d,status:%d\n", AgentNo, status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_MakecallAck(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	uint32_t Mid = (uint32_t)switch_safe_atoi(argv[1], 0);
	uint32_t CalledSSI = (uint32_t)switch_safe_atoi(argv[2], 0);
	uint32_t CallerSSI = (uint32_t)switch_safe_atoi(argv[3], 0);
	uint8_t Opt = (uint8_t)switch_safe_atoi(argv[4], 0);
	status = PDT_API_MakecallAck(AgentNo, Mid, CalledSSI, CallerSSI, Opt);
	stream->write_function(stream, "MakecallAck AgentNo:%d,status:%d\n", AgentNo, status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_Quit(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	uint32_t Mid = (uint32_t)switch_safe_atoi(argv[1], 0);
	uint8_t ChanNum = (uint8_t)switch_safe_atoi(argv[2], 0);
	uint8_t ReqFrom = (uint8_t)switch_safe_atoi(argv[3], 0);
	uint8_t Opt = (uint8_t)switch_safe_atoi(argv[4], 0);
	status = PDT_API_Quit(AgentNo, Mid, ChanNum, ReqFrom, Opt);
	stream->write_function(stream, "Quit AgentNo:%d,status:%d\n", AgentNo, status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_PTT(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	uint32_t Mid = (uint32_t)switch_safe_atoi(argv[1], 0);
	uint32_t CallerSSI = (uint32_t)switch_safe_atoi(argv[2], 0);
	uint8_t Opt = (uint8_t)switch_safe_atoi(argv[3], 0);

	status = PDT_API_PTT(AgentNo, Mid, CallerSSI, Opt);
	stream->write_function(stream, "PTT AgentNo:%d,status:%d\n", AgentNo, status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_SMS(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	uint8_t AgentNo = (uint8_t)switch_safe_atoi(argv[0], 0);
	uint32_t CalledSSI = (uint32_t)switch_safe_atoi(argv[1], 0);
	uint32_t CallerSSI = (uint32_t)switch_safe_atoi(argv[2], 0);
	uint16_t Ctype = (uint16_t)switch_safe_atoi(argv[3], 0);
	char* Content = argv[4];
	status = PDT_API_SMS(AgentNo, CalledSSI, CallerSSI, Ctype, Content, (uint16_t)strlen(Content));
	stream->write_function(stream, "AgentNo:%d,status:%d\n", AgentNo, status);
	return SWITCH_STATUS_SUCCESS;
}
char* getApiStatusMsg(ENUM_API_STATUS status)
{
	char msg[100] = { 0 };
	switch (status)
	{
	case API_STATUS_SUCC:
		strcpy(msg, "执行成功");
		break;
	case API_STATUS_FALSE:
		strcpy(msg, "执行失败");
		break;
	case API_STATUS_TIMEOUT:
		strcpy(msg, "执行超时");
		break;
	}
	return switch_core_strdup(pdt_globals.pool, msg);
}
char* MakeJson_Status(ENUM_API_STATUS nStatus,int nRetCode) {
	char *f = NULL;
	cJSON *obj = cJSON_CreateObject();
	cJSON_AddItemToObject(obj, "status", cJSON_CreateNumber(nStatus));
	cJSON_AddItemToObject(obj, "msg", cJSON_CreateString(getApiStatusMsg(nStatus)));
	f = cJSON_PrintUnformatted(obj);
	return f;
}
static switch_status_t cmd_pdt_Manage(char **argv, int argc, switch_stream_handle_t *stream)
{
	char *jsonstatus = NULL;
	switch_status_t status = SWITCH_STATUS_SUCCESS;
	uint32_t RadioSSI = (uint32_t)switch_safe_atoi(argv[0], 0);
	uint8_t Ret = API_STATUS_TIMEOUT;
	uint8_t Opt = (uint8_t)switch_safe_atoi(argv[1], 0);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "cmd_pdt_Manage RadioSSI:%d Opt:%d\n", RadioSSI, Opt);
	switch_mutex_lock(pdt_globals.mutex_api_manage);
	status = PDT_API_MANAGE(0, RadioSSI, Opt);
	if (switch_thread_cond_timedwait(pdt_globals.cond_api_manage, pdt_globals.mutex_api_manage, PDT_CHANNEL_TIMEOUT_USEC) == SWITCH_STATUS_SUCCESS) {
		if (pdt_globals.Ret_api_manage == 0) {
			Ret = API_STATUS_SUCC;
		}
		else {
			Ret = API_STATUS_FALSE;
		}
	}
	jsonstatus = MakeJson_Status(Ret, pdt_globals.Ret_api_manage);
	stream->write_function(stream, "%s\n", jsonstatus);

	switch_mutex_unlock(pdt_globals.mutex_api_manage);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t pdt_Session_Record(switch_core_session_t *session, switch_bool_t bStart, char** ppfile)
{
	pdt_profile_t *profile = pdt_globals.profile;
	switch_channel_t *channel = NULL;
	switch_status_t status = SWITCH_STATUS_FALSE;
	if (session) {
		//tech_pvt = (private_object_t *)switch_core_session_get_private(session);
		channel = switch_core_session_get_channel(session);
	}
	if (channel == NULL)
	{
		return status;
	}
	if (bStart == SWITCH_TRUE) {
		char *file = NULL, *tmp = NULL;
		if (ppfile != NULL)
		{
			file = *ppfile;
		}
		else {
			tmp = switch_mprintf("%s%s%s", profile->record_path ? profile->record_path : "${recordings_dir}",
				SWITCH_PATH_SEPARATOR, profile->record_template);
			file = switch_channel_expand_variables(channel, tmp);
		}
		switch_ivr_record_session(session, file, 0, NULL);
		switch_channel_set_variable(channel, "pdt_record_file", file);
		switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_NOTICE, "Recording %s to %s\n", switch_channel_get_name(channel),
			file);
		switch_safe_free(tmp);
		status = SWITCH_STATUS_SUCCESS;
		if (file != profile->record_template) {
			free(file);
			file = NULL;
		}
	}
	else {
		const char *file;
		if ((file = switch_channel_get_variable(channel, "pdt_record_file"))) {
			switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_NOTICE, "Done recording %s to %s\n",
				switch_channel_get_name(channel), file);
			switch_ivr_stop_record_session(session, file);
			status = SWITCH_STATUS_SUCCESS;
			if (ppfile != NULL)
			{
				*ppfile = (char*)file;
			}
		}
		else {
			switch_log_printf(SWITCH_CHANNEL_SESSION_LOG(session), SWITCH_LOG_NOTICE, "recording %s Nothing to stop\n",
				switch_channel_get_name(channel));
		}
	}
	return status;
}

static switch_status_t cmd_pdt_Record(char **argv, int argc, switch_stream_handle_t *stream)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	// 	status = pdt_Session_Record(pdt_globals.cur_session,SWITCH_TRUE,NULL);
	stream->write_function(stream, "Record status:%d\n", status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_RecordStop(char **argv, int argc, switch_stream_handle_t *stream)
{
	// 	switch_status_t status = SWITCH_STATUS_FALSE;
	// 	char* file = NULL;
	// 	char* fCode = NULL;
	// 	status = pdt_Session_Record(pdt_globals.cur_session, SWITCH_FALSE, &file);
	// 	if (argc > 0)
	// 	{
	// 		if (atoi(argv[0]) == 1)
	// 		{
	// 			fCode = IM_Postfile(file);
	// 			if (fCode)
	// 			{
	// 				
	// 				pdt_pvt_t *tech_pvt = switch_core_session_get_private_class(pdt_globals.cur_session, SWITCH_PVT_SECONDARY);
	// 				char *CalledSSI = switch_mprintf("%d", tech_pvt->CalledSSI);
	// 				status = IM_pushMsg("Person", CalledSSI, NULL, fCode);
	// 				switch_safe_free(CalledSSI);
	// 
	// 			}
	// 			stream->write_function(stream, "IM_Postfile fCode:%s\n", fCode);
	// 		}
	// 	}
	// 	stream->write_function(stream, "Record stop status:%d\n", status);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_call(char **argv, int argc, switch_stream_handle_t *stream)
{
	MakeCall_FromPdt((uint32_t)switch_safe_atoi(argv[0], 0),
		(uint32_t)switch_safe_atoi(argv[1], 0),
		(uint32_t)switch_safe_atoi(argv[2], 0),
		1);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_SendPTT(char **argv, int argc, switch_stream_handle_t *stream)
{
// 	Pdt_SendEvent_PTT((uint32_t)switch_safe_atoi(argv[0], 0),
// 		(uint16_t)switch_safe_atoi(argv[1], 0));
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_pushmsg(char **argv, int argc, switch_stream_handle_t *stream)
{
	IM_pushMsg("Group", argv[0], argv[1], NULL,NULL);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_groupid(char **argv, int argc, switch_stream_handle_t *stream)
{
	char* code = IM_getPdtAndAppGroup(argv[0], (uint16_t)switch_safe_atoi(argv[1], 0));
	stream->write_function(stream, "%s\n", code);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_getpdt(char **argv, int argc, switch_stream_handle_t *stream)
{
	char* id = IM_getAppUserID(argv[0], (uint32_t)switch_safe_atoi(argv[1], 0));
	stream->write_function(stream, "%s\n", id);
	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_listcall(char **argv, int argc, switch_stream_handle_t *stream)
{
	pdt_call_info_t *rel = NULL;
	switch_mutex_lock(pdt_globals.mutex_get_call_info);
	for (rel = pdt_globals.get_call_infos; rel; rel = rel->next) {
		
			stream->write_function(stream, "CalledSSI:%d,CallerSSI:%d,Mid:%d,CallType:%d,CallFromType:%d\n",
				rel->CalledSSI, rel->CallerSSI, rel->Mid, rel->CallType, rel->CallFromType);

	}
	switch_mutex_unlock(pdt_globals.mutex_get_call_info);

	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_pdt_listevent(char **argv, int argc, switch_stream_handle_t *stream)
{
	pdt_event_list_t *rel = NULL;
	switch_mutex_lock(pdt_globals.mutex_event_list);
	for (rel = pdt_globals.event_list; rel; rel = rel->next) {

		stream->write_function(stream, "id:%d,nRetryCount:%d\n",
			rel->id,rel->nRetryCount);

	}
	switch_mutex_unlock(pdt_globals.mutex_event_list);

	return SWITCH_STATUS_SUCCESS;
}
static switch_status_t cmd_status(char **argv, int argc, switch_stream_handle_t *stream)
{

	return SWITCH_STATUS_SUCCESS;
}

static switch_status_t cmd_xml_status(char **argv, int argc, switch_stream_handle_t *stream)
{

	return SWITCH_STATUS_SUCCESS;
}

switch_status_t pdt_cmd_main(const char *cmd, switch_core_session_t *session, switch_stream_handle_t *stream)
{
	char *argv[1024] = { 0 };
	int argc = 0;
	char *mycmd = NULL;
	switch_status_t status = SWITCH_STATUS_SUCCESS;
	pdt_command_t func = NULL;
	int lead = 1;
	static const char usage_string[] = "USAGE:\n"
		"--------------------------------------------------------------------------------\n"
		"pdt [status|xmlstatus]\n"
		"pdt help\n"
		"--------------------------------------------------------------------------------\n";

	if (zstr(cmd)) {
		stream->write_function(stream, "%s", usage_string);
		goto done;
	}

	if (!(mycmd = strdup(cmd))) {
		status = SWITCH_STATUS_MEMERR;
		goto done;
	}

	if (!(argc = switch_separate_string(mycmd, ' ', argv, (sizeof(argv) / sizeof(argv[0])))) || !argv[0]) {
		stream->write_function(stream, "%s", usage_string);
		goto done;
	}

	if (!strcasecmp(argv[0], "help")) {
		stream->write_function(stream, "%s", usage_string);
		goto done;
	}
	else if (!strcasecmp(argv[0], "status")) {
		func = cmd_status;
	}
	else if (!strcasecmp(argv[0], "xmlstatus")) {
		func = cmd_xml_status;
	}
	else if (!strcasecmp(argv[0], "login")) {
		func = cmd_pdt_login;
	}
	else if (!strcasecmp(argv[0], "heartbeat")) {
		func = cmd_pdt_Heartbeat;
	}
	else if (!strcasecmp(argv[0], "makecall")) {
		func = cmd_pdt_Makecall;
	}
	else if (!strcasecmp(argv[0], "makecallAck")) {
		func = cmd_pdt_MakecallAck;
	}
	else if (!strcasecmp(argv[0], "quit")) {
		func = cmd_pdt_Quit;
	}
	else if (!strcasecmp(argv[0], "ptt")) {
		func = cmd_pdt_PTT;
	}
	else if (!strcasecmp(argv[0], "sms")) {
		func = cmd_pdt_SMS;
	}
	else if (!strcasecmp(argv[0], "manage")) {
		func = cmd_pdt_Manage;
	}
	else if (!strcasecmp(argv[0], "record")) {
		func = cmd_pdt_Record;
	}
	else if (!strcasecmp(argv[0], "recordstop")) {
		func = cmd_pdt_RecordStop;
	}
	else if (!strcasecmp(argv[0], "call")) {
		func = cmd_pdt_call;
	}
	else if (!strcasecmp(argv[0], "sendptt")) {
		func = cmd_pdt_SendPTT;
	}
	else if (!strcasecmp(argv[0], "pushmsg")) {
		func = cmd_pdt_pushmsg;
	}
	else if (!strcasecmp(argv[0], "groupid")) {
		func = cmd_pdt_groupid;
	}
	else if (!strcasecmp(argv[0], "getpdt")) {
		func = cmd_pdt_getpdt;
	}
	else if (!strcasecmp(argv[0], "listcall")) {
		func = cmd_pdt_listcall;
	}
	else if (!strcasecmp(argv[0], "listevent")) {
		func = cmd_pdt_listevent;
	}
	if (func) {
		status = func(&argv[lead], argc - lead, stream);
	}
	else {
		stream->write_function(stream, "Unknown Command [%s]\n", argv[0]);
	}

done:
	switch_safe_free(mycmd);
	return status;


}