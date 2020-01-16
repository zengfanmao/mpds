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

//�ͻ��˵�¼/�˳�
typedef struct {
	uint16_t AgentNo;
	uint32_t TelNum;
	uint32_t InSSI;
	uint32_t OutSSI;
	uint8_t Opt;
}PDTDATA_LoginLogout;

//�������Ӧ��¼/�˳�
typedef struct {
	uint16_t AgentNo;
	uint8_t Ret;
	uint8_t ChanSum;
	uint32_t VoiceIP;
	uint16_t VoicePort;
}PDTDATA_LoginLogoutAck;

//�ͻ��˷�������
typedef struct {
	uint16_t AgentNo;
	uint32_t AgentStatus;
} PDTDATA_HeartBeat;

//�������Ӧ����
typedef struct {
	uint16_t AgentNo;
// 	uint8_t Chansum;
} PDTDATA_HeartBeatAck;


typedef enum {
	CALL_Type_Single = 0,
	CALL_Type_Multicast = 1
} ENUM_CALL_TYPE;

//�ͻ��˺���
typedef struct {
	uint16_t AgentNo;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint8_t IsSimulate;
	uint16_t CallType;//����/���/����ͬ����/������У�=0x00������=0x01�����
	uint16_t CallTime;
	uint8_t Conv;//�������ͣ�=0��Ⱥ��PDT����=1���� ��=3 ���浥��
}PDTDATA_Makecall;

//�ͻ�����ӦPDTϵͳ�ĺ��С��������廰
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint8_t Opt;//0	��Ӧ���У�˫���������ͻ��˼���   1	���ü����������������ͻ��˼���  	2	��ʼ�廰�����PDTϵͳ���ڷ������նˣ��ͻ��˿��Կ�ʼ������˫���������ͻ��˼���
}PDTDATA_MakecallAck;

//PDTϵͳ�к���
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t CallType;//���е����ͣ�0x00=������0x01=�����0x02=����ͬ���飨���ã�
	uint16_t CallTime;//��������ʱ����λ�룬0xFFFF����ʱ
	uint8_t CallGrade;//���е����ȼ�0x00=��ͨ��0x01=������0x02=�㲥����
}PDTDATA_ServerGetCall;

//����˷���ͨ��
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint8_t ChanNum;
	uint16_t FailReason;//0	�ɹ�   1	û���ҵ���ػ��� 	2	û�п�����Ƶͨ��
}PDTDATA_ServerSelectedChan;

//��̨���Ϣ
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t RadioSSI;
	uint32_t CalledSSI;
	uint16_t RadioType;//��̨���ͣ�0x00=���ֵ�̨��0x01=ģ���̨
	uint16_t ActionType;//����ͣ�0x03=PTT��Ϣ�����ã�����0x00=��¼��0x01=�������룬0x02=Ӧ��0x04=������Ϣ��
	uint16_t PTTType;//ActionType 0x03, PTT��Ϣ��PTTType = 0��̨���ߣ� = 1 PTTON�����ã��� = 2 PTTOFF�����ã���
}PDTDATA_ServerActionInfo;

//���ߡ��˳���ǿ��
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint8_t ChanNum;
	uint8_t ReqFrom;//=0 ���Է���ˣ�=1���Կͻ��ˣ��ͻ��˷��𣬷����Ӧ�𣬶���=1��
	uint8_t Opt;//0	����	1	�˳� 		2	ǿ��  	3	�˳�����
}PDTDATA_Quit;

//����̨PTT
typedef struct {
	uint16_t AgentNo;
	uint32_t Mid;
	uint32_t CallerSSI;
	uint8_t Opt;//1	PTTON��ʼ���� 	2	PTTOFFֹͣ����

}PDTDATA_PTT;

//�շ�����Ϣ
typedef struct {
	uint16_t AgentNo;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t Ctype;//��������,0x00=����,0x01=���������ȫ���ţ�
}PDTDATA_SMS;

typedef struct {
	uint16_t AgentNo;
	uint32_t CalledSSI;
	uint32_t CallerSSI;
	uint16_t Ctype;		//��������,0x00=����,0x01=���������ȫ���ţ�
	uint8_t Ret;//Ӧ����������ʱ��=0 ���ͳɹ���=1����ʧ�ܣ������=1���ͳɹ�
}PDTDATA_SMS_Ack;

//��̨����
typedef struct {
	uint16_t AgentNo;
	uint32_t RadioSSI;
	uint16_t Opt;  //0 ң��  1 ң��	  2 ����
}PDTDATA_MANAGE;

typedef struct {
	uint16_t AgentNo;
	uint32_t RadioSSI;
	uint16_t Opt;  //0 ң��  1 ң��	  2 ����
	uint8_t Ret; //0 �ɹ�	1 ʧ��
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