#ifndef PDT_API_HEAD_H
#define PDT_API_HEAD_H

#include <switch.h>
// const char* const GetDirections[H323Channel::NumDirections+1] = {
// 	"IsBidirectional",
// 	"IsTransmitter",
// 	"IsReceiver",
// 	"NumDirections"
// };

typedef enum {
	DATA_ID_NULL,
	DATA_ID_LOGIN_LOGOUT,
	DATA_ID_LOGIN_LOGOUT_ACK,
	DATA_ID_HeartBeat,
	DATA_ID_HeartBeatAck,
	DATA_ID_Makecall,
	DATA_ID_MakecallAck,
	DATA_ID_ServerGetCall,
	DATA_ID_ServerSelectedChan,
	DATA_ID_ServerActionInfo,
	DATA_ID_Quit,
	DATA_ID_PTT,
	DATA_ID_SMS,
	DATA_ID_SMS_Ack,
	DATA_ID_MANAGE,
	DATA_ID_MANAGE_Ack,
	DATA_ID_MAX
} ENUM_PDT_DATA;

#pragma pack(1)
typedef struct {
	uint8_t SYNC[2];
	uint16_t LEN;
	uint8_t TYPE;
	uint8_t VER;
	uint8_t CMD;
	uint8_t CMDX;
}PDT_HEAD;

typedef struct {
	uint8_t ID;
	uint8_t TYPE;
	uint8_t CMD;
	uint8_t CMDX;
}PDT_HEAD_TYPE;

//客户端登录/退出
typedef struct {
	uint16_t AgentNo;
	uint32_t TelNum;
	uint32_t InSSI;
	uint32_t OutSSI;
	uint8_t Opt;
}PDTDATA_LoginLogout;

//服务端响应登录/退出
typedef struct {
	uint16_t AgentNo;
	uint8_t Ret;
	uint8_t ChanSum;
	uint32_t VoiceIP;
	uint16_t VoicePort;
}PDTDATA_LoginLogoutAck;

//客户端发送心跳
typedef struct {
	uint16_t AgentNo;
	uint32_t AgentStatus;
} PDTDATA_HeartBeat;

//服务端响应心跳
typedef struct {
	uint16_t AgentNo;
// 	uint8_t Chansum;
} PDTDATA_HeartBeatAck;


typedef enum {
	CALL_Type_Single = 0,
	CALL_Type_Multicast = 1
} ENUM_CALL_TYPE;

//客户端呼出
typedef struct {
	uint16_t AgentNo;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint8_t IsSimulate;
	uint16_t CallType;//单呼/组呼/被动同播组/常规呼叫，=0x00单呼，=0x01组呼，
	uint16_t CallTime;
	uint8_t Conv;//呼叫类型，=0集群（PDT），=1常规 ，=3 常规单呼
}PDTDATA_Makecall;

//客户端响应PDT系统的呼叫、监听、插话
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint8_t Opt;//0	响应呼叫（双向语音）客户端加入   1	设置监听（单向语音）客户端加入  	2	开始插话（拆除PDT系统正在发话的终端，客户端可以开始发话，双向语音）客户端加入
}PDTDATA_MakecallAck;

//PDT系统有呼叫
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t CallType;//呼叫的类型：0x00=单呼，0x01=组呼，0x02=被动同播组（不用）
	uint16_t CallTime;//呼叫允许时长单位秒，0xFFFF不限时
	uint8_t CallGrade;//呼叫的优先级0x00=普通，0x01=紧急，0x02=广播呼叫
}PDTDATA_ServerGetCall;

//服务端分配通道
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint8_t ChanNum;
	uint16_t FailReason;//0	成功   1	没有找到相关会议 	2	没有空闲音频通道
}PDTDATA_ServerSelectedChan;

//电台活动消息
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t RadioSSI;
	uint32_t CalledSSI;
	uint16_t RadioType;//电台类型，0x00=数字电台，0x01=模拟电台
	uint16_t ActionType;//活动类型，0x03=PTT信息（采用），（0x00=登录，0x01=呼叫申请，0x02=应答，0x04=其他消息）
	uint16_t PTTType;//ActionType 0x03, PTT信息，PTTType = 0电台撤线， = 1 PTTON（采用）， = 2 PTTOFF（采用）。
}PDTDATA_ServerActionInfo;

//拆线、退出、强拆
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint8_t ChanNum;
	uint8_t ReqFrom;//=0 来自服务端，=1来自客户端（客户端发起，服务端应答，都是=1）
	uint8_t Opt;//0	拆线	1	退出 		2	强拆  	3	退出常规
}PDTDATA_Quit;

//调度台PTT
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CallerSSI;
	uint8_t Opt;//1	PTTON开始讲话 	2	PTTOFF停止讲话

}PDTDATA_PTT;

//收发短消息
typedef struct {
	uint16_t AgentNo;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t Ctype;//呼叫类型,0x00=单呼,0x01=组呼（包括全网号）
}PDTDATA_SMS;

typedef struct {
	uint16_t AgentNo;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t Ctype;		//呼叫类型,0x00=单呼,0x01=组呼（包括全网号）
	uint8_t Ret;//应答结果，单呼时：=0 发送成功，=1发送失败；组呼：=1发送成功
}PDTDATA_SMS_Ack;

//电台管理
typedef struct {
	uint16_t AgentNo;
	uint32_t RadioSSI;
	uint16_t Opt;  //0 遥晕  1 遥毙	  2 复活
}PDTDATA_MANAGE;

typedef struct {
	uint16_t AgentNo;
	uint32_t RadioSSI;
	uint16_t Opt;  //0 遥晕  1 遥毙	  2 复活
	uint8_t Ret; //0 成功	1 失败
}PDTDATA_MANAGE_Ack;


#pragma pack()
void PDT_InitHead(PDT_HEAD* pPdtHead,int dataID);
switch_bool_t PDT_IsHead(PDT_HEAD* pPdtHead);
switch_bool_t PDT_GetApiHead(ENUM_PDT_DATA apiID, PDT_HEAD* pHead);
ENUM_PDT_DATA PDT_GetApiID(PDT_HEAD head);
switch_status_t PDT_Data_Send(ENUM_PDT_DATA DataType,char* pData,int nDataLen);
switch_status_t PDT_Data_Send_base(switch_socket_t *sock, ENUM_PDT_DATA DataType, char* pData, int nDataLen);

switch_status_t PDT_API_Login(uint16_t AgentNo);
switch_status_t PDT_API_Logout(uint16_t AgentNo);
switch_status_t PDT_API_Heartbeat(uint16_t AgentNo);
switch_status_t PDT_API_Makecall(uint16_t AgentNo,uint32_t CalledSSI, uint32_t CallerSSI, uint8_t IsSimulate,uint16_t CallType,uint16_t CallTime,uint8_t Conv);
switch_status_t PDT_API_MakecallAck(switch_socket_t *sock, uint16_t AgentNo, uint32_t Mid, uint32_t CalledSSI, uint32_t CallerSSI, uint8_t Opt);
switch_status_t PDT_API_Quit(switch_socket_t *sock, uint16_t AgentNo, uint32_t Mid, uint8_t ChanNum, uint8_t ReqFrom, uint8_t Opt);
switch_status_t PDT_API_PTT(switch_socket_t *sock, uint16_t AgentNo, uint32_t Mid, uint32_t CallerSSI, uint8_t Opt);
switch_status_t PDT_API_ServerSelectedChan(switch_socket_t *sock, uint16_t AgentNo, uint32_t Mid, uint32_t CalledSSI, uint32_t CallerSSI);
switch_status_t PDT_API_ServerGetCall(switch_socket_t *sock, uint16_t AgentNo, uint32_t Mid, uint32_t CalledSSI, uint32_t CallerSSI, uint16_t CallType);

switch_status_t PDT_API_SMS(switch_socket_t *sock, uint16_t AgentNo, uint32_t CalledSSI, uint32_t CallerSSI, uint16_t Ctype, char* Content, uint16_t ContentLen);
switch_status_t PDT_API_SMS_Ack(switch_socket_t *sock, uint16_t AgentNo, uint32_t CalledSSI, uint32_t CallerSSI, uint16_t Ctype,uint8_t Ret);

switch_status_t PDT_API_MANAGE(uint16_t AgentNo, uint32_t RadioSSI, uint16_t Opt);

// 
// class FSH323_T38Capability
// {
//     PCLASSINFO(FSH323_T38Capability, H323_T38Capability);
//   public:
//     FSH323_T38Capability(const OpalMediaFormat &_mediaFormat)
//       : H323_T38Capability(e_UDP),
//         mediaFormat(_mediaFormat) {
// 	}
//     virtual PObject * Clone() const {
// 		return new FSH323_T38Capability(*this);
// 	}
//     virtual PString GetFormatName() const { 
// 		return mediaFormat; 
// 	}
//     virtual H323Channel * CreateChannel(
//       H323Connection & connection,
//       H323Channel::Directions dir,
//       unsigned sessionID,
//       const H245_H2250LogicalChannelParameters * param    ) const;
//   protected:
//     const OpalMediaFormat &mediaFormat;
// };


#endif //PDT_API_HEAD_H