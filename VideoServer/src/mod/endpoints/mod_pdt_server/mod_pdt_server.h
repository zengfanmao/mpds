#ifndef MOD_PDT_SERVER_H
#define MOD_PDT_SERVER_H
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

#define PDT_CHAT_PROTO "pdt_server"
#define PDT_EVENT_MAINT "pdt_server::event"

#define MAX_BIND 25
#define MAX_RTPIP 25
#define PTT_STATE_ON		1
#define PTT_STATE_OFF		0

#define copy_string(x,y,z) strncpy(x, y, z - 1) 
#define set_string(x,y) strncpy(x, y, sizeof(x)-1) 

#define PDT_CHANNEL_TIMEOUT_USEC (5000 * 1000)

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

typedef enum {
	LFLAG_AUTHED = (1 << 0),
	LFLAG_RUNNING = (1 << 1),
	LFLAG_EVENTS = (1 << 2),
	LFLAG_LOG = (1 << 3),
	LFLAG_FULL = (1 << 4),
	LFLAG_MYEVENTS = (1 << 5),
	LFLAG_SESSION = (1 << 6),
	LFLAG_ASYNC = (1 << 7),
	LFLAG_STATEFUL = (1 << 8),
	LFLAG_OUTBOUND = (1 << 9),
	LFLAG_LINGER = (1 << 10),
	LFLAG_HANDLE_DISCO = (1 << 11),
	LFLAG_CONNECTED = (1 << 12),
	LFLAG_RESUME = (1 << 13),
	LFLAG_AUTH_EVENTS = (1 << 14),
	LFLAG_ALL_EVENTS_AUTHED = (1 << 15),
	LFLAG_ALLOW_LOG = (1 << 16)
} event_flag_t;

typedef enum {
	EVENT_FORMAT_PLAIN,
	EVENT_FORMAT_XML,
	EVENT_FORMAT_JSON
} event_format_t;

struct listener {
	switch_socket_t *sock;
	switch_queue_t *event_queue;
	switch_queue_t *log_queue;
	switch_memory_pool_t *pool;
	event_format_t format;
	switch_mutex_t *flag_mutex;
	switch_mutex_t *filter_mutex;
	uint32_t flags;
	switch_log_level_t level;
	char *ebuf;
	uint8_t event_list[SWITCH_EVENT_ALL + 1];
	uint8_t allowed_event_list[SWITCH_EVENT_ALL + 1];
	switch_hash_t *event_hash;
	switch_hash_t *allowed_event_hash;
	switch_hash_t *allowed_api_hash;
	switch_thread_rwlock_t *rwlock;
	switch_core_session_t *session;
	int lost_events;
	int lost_logs;
	time_t last_flush;
	time_t expire_time;
	uint32_t timeout;
	uint32_t id;
	switch_sockaddr_t *sa;
	char remote_ip[50];
	switch_port_t remote_port;
	switch_event_t *filters;
	time_t linger_timeout;
	struct listener *next;
	switch_pollfd_t *pollfd;
};

typedef struct listener listener_t;

typedef struct globals_s {
	switch_mutex_t *listener_mutex;
	switch_event_node_t *node;
	switch_mutex_t *mutex;
	switch_memory_pool_t *pool;
	switch_socket_t *sock;
	switch_bool_t bPDTConnected;
	switch_time_t timeLastHeartbeat;
	uint8_t PdtServerChanSum;
	char PdtServerIP[50];
	uint16_t PdtServerPort;
	char PdtServerVoiceIP[50];
	uint16_t PdtServerVoicePort;
	uint16_t PdtServerVoiceChanNum;
	int UsePdt;
	uint16_t AgentNo;
	uint32_t CallerSSI;
	uint32_t CalledSSI;
	uint32_t RadioSSI;
	uint32_t Mid;
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



	/** synchronizes channel state */
	switch_mutex_t *mutex_api_manage;
	/** wait on channel states */
	switch_thread_cond_t *cond_api_manage;
	uint8_t Ret_api_manage;

	listener_t *listener;

} globals_t;

void send_rtp();
void send_rtp_stop();
void send_quit();
switch_size_t trans(const char *pFromCode, const char *pToCode, const char *pInBuf, size_t iInLen, char *pOutBuf, size_t iOutLen);

/** @} */
#endif

