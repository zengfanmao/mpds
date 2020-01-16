#ifndef MOD_PDT_H
#define MOD_PDT_H
#include <switch.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#ifdef WIN32
#include <WinSock2.h>
#else
#include <sys/socket.h>
#include <arpa/inet.h>
#include <sys/wait.h> 
#endif
#include <string.h>
#ifndef WIN32
#include <unistd.h>
#include <poll.h>
#endif
#include <stdarg.h>
#ifndef WIN32
#include <netinet/tcp.h>
#include <sys/un.h>
#endif
#include <assert.h>
#include <errno.h>
#ifndef WIN32
#include <pwd.h>
#include <netdb.h>
#endif

#define PDT_CHAT_PROTO "pdt"
#define PDT_EVENT_MAINT "pdt::event"
#define CONF_EVENT_MAINT "conference::maintenance"

#define MAX_BIND 25
#define MAX_RTPIP 25
#define PTT_STATE_ON		1
#define PTT_STATE_OFF		0

#define copy_string(x,y,z) strncpy(x, y, z - 1) 
#define set_string(x,y) strncpy(x, y, sizeof(x)-1) 

#define PDT_CHANNEL_TIMEOUT_USEC (5000 * 1000)


typedef enum {
	CALL_Type_Single = 0,
	CALL_Type_Multicast = 1
} ENUM_CALL_TYPE;
typedef enum {
	CALL_From_App = 0,
	CALL_From_Pdt = 1
} ENUM_CALL_FROM_TYPE;

typedef enum {
	GET_All = 0,
	GET_Pdt = 1,
	GET_App = 2
} ENUM_GET_TYPE;

#define USE_pdtcalllist		//使用队列缓存PDT会话状态

typedef enum {
	API_STATUS_SUCC  = 0,
	API_STATUS_TIMEOUT = 8,
	API_STATUS_FALSE = 9,
	API_STATUS_MAX
} ENUM_API_STATUS;

typedef struct pdt_pvt_s {
	char *jsock_uuid;
	char *call_id;
	char *r_sdp;
	switch_core_session_t *session;
	switch_channel_t *channel;
	switch_media_handle_t *smh;
	switch_media_handle_t *media_handle;
	switch_core_media_params_t *mparams;
	switch_core_media_params_t mediaParams;

	switch_call_cause_t remote_hangup_cause;
	time_t detach_time;
	struct pdt_pvt_s *next;

	unsigned int flags;
	switch_mutex_t *flag_mutex;
	switch_mutex_t *mutex;
	switch_core_session_t *other_session;
	struct pdt_pvt_s *other_tech_pvt;
	switch_channel_t *other_channel;
	switch_codec_t read_codec;
	switch_codec_t write_codec;
	switch_frame_t read_frame;
	unsigned char databuf[SWITCH_RECOMMENDED_BUFFER_SIZE];

	switch_frame_t *x_write_frame;
	switch_frame_t *write_frame;
	unsigned char write_databuf[SWITCH_RECOMMENDED_BUFFER_SIZE];

	switch_frame_t cng_frame;
	unsigned char cng_databuf[SWITCH_RECOMMENDED_BUFFER_SIZE];
	switch_timer_t timer;
	switch_caller_profile_t *caller_profile;
	int32_t bowout_frame_count;
	char *other_uuid;
	switch_queue_t *frame_queue;
	int64_t packet_count;
	int first_cng;

	uint16_t rtp_local_port;
	uint16_t rtp_remote_port;
	char *rtp_local_ip;
	char *rtp_remote_ip;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t Multicast;
	uint32_t Mid;
	uint8_t ChanNum;
} pdt_pvt_t;

typedef struct pdt_get_call_info {
//	PDTDATA_ServerGetCall ServerGetCall;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t CallType;//呼叫的类型：0x00=单呼，0x01=组呼，0x02=被动同播组（不用）
	uint16_t CallFromType;
	struct pdt_get_call_info *next;
} pdt_call_info_t;

typedef struct pdt_event_list {
	//	PDTDATA_ServerGetCall ServerGetCall;
	uint32_t id;
	uint32_t nRetryCount;
	switch_event_t *event;
	struct pdt_event_list *next;
} pdt_event_list_t;

typedef struct ips {
	char local_ip[256];
	uint16_t local_port;
	int secure;
	int family;
} ips_t;

typedef struct pdt_profile_s {
	char *name;
	switch_mutex_t *mutex;
	switch_memory_pool_t *pool;
	switch_thread_rwlock_t *rwlock;

	struct ips ip[MAX_BIND];
	int i;

	int running;

	int ready;
	int debug;

	int in_thread;
	int blind_reg;

	char *userauth;
	char *root_passwd;

	char *context;
	char *dialplan;

	char *mcast_ip;
	switch_port_t mcast_port;

	char *extrtpip;

	char *rtpip[MAX_RTPIP];
	int rtpip_index;
	int rtpip_cur;

	char *rtpip6[MAX_RTPIP];
	int rtpip_index6;
	int rtpip_cur6;

	char *cand_acl[SWITCH_MAX_CAND_ACL];
	uint32_t cand_acl_count;

	char *conn_acl[SWITCH_MAX_CAND_ACL];
	uint32_t conn_acl_count;

	char *inbound_codec_string;
	char *outbound_codec_string;

	char *timer_name;
	char *local_network;

	char *register_domain;

	char *record_template;
	char *record_path;

	struct pdt_profile_s *next;
}pdt_profile_t;

typedef struct globals_s {
	switch_mutex_t *mutex;
	switch_memory_pool_t *pool;
	switch_socket_t *sock;
	switch_bool_t bPDTConnected;
	switch_bool_t bImConnected;
	switch_bool_t bIsDebugServer;//是调试服务器
	switch_time_t timeLastHeartbeat;
	uint8_t PdtServerChanSum;
	char PdtServerIP[50];
	uint16_t PdtServerPort;
	char PdtServerVoiceIP[50];
	uint16_t PdtServerVoicePort;
	uint16_t PdtServerVoiceChanNum;
	int UsePdt;
	int CheckServerGetCall;
	uint16_t AgentNo;
	uint32_t CallerSSI;
	int sig;
	int running;
	uint32_t flags;
// 	uint32_t Mid;
	switch_hash_t *method_hash;
	switch_mutex_t *method_mutex;

	switch_hash_t *event_channel_hash;
	switch_thread_rwlock_t *event_channel_rwlock;

	int debug;
	int ready;
	pdt_pvt_t *tech_head;
	switch_thread_rwlock_t *tech_rwlock;

	switch_thread_cond_t *detach_cond;
	switch_mutex_t *detach_mutex; 
	switch_mutex_t *detach2_mutex; 

	uint32_t detached;
	uint32_t detach_timeout;

	switch_thread_t *threadRecv;
	switch_thread_t *threadConnect;

// 	pdt_profile_t *profile_head;
	pdt_profile_t *profile;
	char *domain_name;
	char *user_full_id;

// 	switch_core_session_t *cur_session;
// 	switch_channel_t *cur_channel;

	char *im_LoginName;
	char *im_UserPwd;
	
	char *im_ServerUrl;
	char *im_ApiToken;
	char *im_UserCode;

	switch_mutex_t *mutex_get_call_info;
	pdt_call_info_t *get_call_infos;

	switch_mutex_t *mutex_event_list;
	pdt_event_list_t *event_list;

	/** synchronizes channel state */
	switch_mutex_t *mutex_api_manage;
	/** wait on channel states */
	switch_thread_cond_t *cond_api_manage;
	uint8_t Ret_api_manage;

	switch_mutex_t *mutex_thread_event_list;
	switch_thread_cond_t *cond_thread_event_list;
	uint32_t threadEventListInterval;
	uint32_t threadEventListRetryCount;
	uint32_t IMStatusCheckInterval;

} globals_t;



switch_size_t trans(const char *pFromCode, const char *pToCode, const char *pInBuf, size_t iInLen, char *pOutBuf, size_t iOutLen);
switch_status_t pdt_cmd_main(const char *cmd, switch_core_session_t *session, switch_stream_handle_t *stream);
switch_status_t MakeCall_FromPdt(uint32_t Mid, uint32_t CalledSSI, uint32_t CallerSSI, uint8_t ChanNum);
void Pdt_SendEvent_PTT(uint32_t Mid, int nPttState, uint32_t CallerSSI, uint32_t CalledSSI);
void Pdt_SendEvent_Quit(uint32_t Mid, uint8_t ChanNum, uint8_t ReqFrom, uint8_t Opt, uint32_t CallerSSI);
void Pdt_event_handler(switch_event_t *event);
void Pdt_event_record_stop(switch_event_t *event);

ENUM_CALL_TYPE ToCallType(char *cMulticast);
pdt_call_info_t *Pdt_add_pdtcallinfo(uint32_t CallFromType, uint32_t CalledSSI, uint32_t CallerSSI, uint32_t CallType, uint32_t Mid);
pdt_call_info_t *Pdt_get_pdtcallinfo_caller(uint32_t CallerSSI);
pdt_call_info_t *Pdt_get_pdtcallinfo_Mid(uint32_t Mid);
switch_status_t Pdt_del_pdtcallinfo_mid(uint32_t CallerSSI);
switch_status_t event_handler_IM_event(switch_event_t *event);
// switch_status_t event_handler_sys_event(switch_event_t *event);
void pdt_launch_thread_Eventlist();
/** @} */
#endif

