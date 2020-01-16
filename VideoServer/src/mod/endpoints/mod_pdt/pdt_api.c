
#include "pdt_api.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "mod_pdt.h"
extern globals_t pdt_globals;

PDT_HEAD_TYPE g_pdtDataType[DATA_ID_MAX] = {
	{ DATA_ID_LOGIN_LOGOUT,			0x20,0xA0,0x01 },
	{ DATA_ID_LOGIN_LOGOUT_ACK,		0x20,0xA0,0x02 },
	{ DATA_ID_HeartBeat,			0x20,0xA0,0x03 },
	{ DATA_ID_HeartBeatAck,			0x20,0xA0,0x00 },
	{ DATA_ID_Makecall,				0x20,0xA0,0x04 },
	{ DATA_ID_MakecallAck,			0x20,0xA0,0x06 },
	{ DATA_ID_ServerGetCall,		0x20,0xA0,0x05 },
	{ DATA_ID_ServerSelectedChan,	0x20,0xA0,0x07 },
	{ DATA_ID_ServerActionInfo,		0x20,0xA0,0x08 },
	{ DATA_ID_Quit,					0x20,0xA0,0x09 },
	{ DATA_ID_PTT,					0x20,0xA0,0x12 },
	{ DATA_ID_SMS,					0x20,0xA0,0x0E },
	{ DATA_ID_SMS_Ack,				0x20,0xA0,0x0F },
	{ DATA_ID_MANAGE,				0x20,0xA0,0x0C },
	{ DATA_ID_MANAGE_Ack,			0x20,0xA0,0x0D }
};

ENUM_PDT_DATA PDT_GetApiID(PDT_HEAD head)
{
	ENUM_PDT_DATA apiID = DATA_ID_NULL;
	for (int i=0;i < DATA_ID_MAX;i ++)
	{
		if (g_pdtDataType[i].TYPE == head.TYPE &&
			g_pdtDataType[i].CMD == head.CMD &&
			g_pdtDataType[i].CMDX == head.CMDX)
		{
			apiID = g_pdtDataType[i].ID;
		}
	}
	if (apiID == DATA_ID_NULL)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "PDT_GetApiID Is Unknown Package,type:%X,CMD:%X,CMDX:%X\n", 
			head.TYPE, head.CMD, head.CMDX);
	} else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "PDT_GetApiID IsapiID:%d\n", apiID);

	}
	return apiID;
}

void PDT_InitHead(PDT_HEAD* pPdtHead,int dataID)
{
	pPdtHead->SYNC[0] = 0xAB;
	pPdtHead->SYNC[1] = 0xAA;
	pPdtHead->VER = 0x10;
	pPdtHead->LEN = sizeof(PDT_HEAD);
	if (dataID >=0 )
	{
		PDT_GetApiHead(dataID, pPdtHead);
	}
}
switch_bool_t PDT_IsHead(PDT_HEAD* pPdtHead)
{
	switch_bool_t bReturn = SWITCH_FALSE;
	if (pPdtHead->SYNC[0] == 0xAB && pPdtHead->SYNC[1] == 0xAA)
	{
		bReturn = SWITCH_TRUE;
	}
	return bReturn;
}
switch_bool_t PDT_GetApiHead(ENUM_PDT_DATA apiID, PDT_HEAD* pHead)
{
	switch_bool_t bReturn = SWITCH_FALSE;
	if (pHead == NULL || apiID > DATA_ID_MAX || apiID < 0)
	{
		return bReturn;
	}
	for (int i = 0;i < DATA_ID_MAX;i++)
	{
		if (g_pdtDataType[i].ID == apiID)
		{
			pHead->TYPE = g_pdtDataType[i].TYPE;
			pHead->CMD = g_pdtDataType[i].CMD;
			pHead->CMDX = g_pdtDataType[i].CMDX;
			bReturn = SWITCH_TRUE;
		}
	}
	return bReturn;
}

switch_status_t PDT_Data_Send(ENUM_PDT_DATA dataID, char* pData, int nDataLen)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	switch_size_t nHeadLen = sizeof(PDT_HEAD);
	PDT_HEAD PdtHead;
	char* cSendBuf = NULL;
	switch_size_t nSendLen = 0;
	if (pdt_globals.sock == NULL)
	{
		return status;
	}

	PDT_InitHead(&PdtHead, dataID);
	PdtHead.LEN += (uint16_t)nDataLen;
	nSendLen = PdtHead.LEN;
	if (nSendLen >= nHeadLen)
	{
		cSendBuf = (char*)malloc(nSendLen);
		memcpy(cSendBuf, &PdtHead, nHeadLen);
		memcpy(cSendBuf + nHeadLen, pData, nDataLen);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "PDT_Data_Send nSendLen:%d\n", nSendLen);
	status = switch_socket_send(pdt_globals.sock, cSendBuf, &nSendLen);
	if (status == SWITCH_STATUS_SUCCESS)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "PDT_Data_Send Succ nSendLen:%d\n", nSendLen);
	}
	else
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "PDT_Data_Send Err status:%d\n", status);
	}
	if (cSendBuf != NULL)
	{
		free(cSendBuf);
		cSendBuf = NULL;
	}
	return status;
}
switch_status_t PDT_API_LoginLogout(uint16_t AgentNo,int opt)
{
	PDTDATA_LoginLogout struLoginLogout;
	int nLen = sizeof(PDTDATA_LoginLogout);
	memset(&struLoginLogout,0, nLen);
	struLoginLogout.AgentNo = AgentNo;
	struLoginLogout.Opt = 1;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT_API_Login(%d)\n", AgentNo);

	return PDT_Data_Send(DATA_ID_LOGIN_LOGOUT, (void*)&struLoginLogout, nLen);
}
switch_status_t PDT_API_Login(uint16_t AgentNo)
{
	return PDT_API_LoginLogout(AgentNo, 1);
}

switch_status_t PDT_API_Logout(uint16_t AgentNo)
{
	return PDT_API_LoginLogout(AgentNo, 2);
}
#define PDTData_InitWithAgentNo(obj, AgentNo) sizeof(obj); \
	memset(&obj, 0, sizeof(obj)); obj.AgentNo = AgentNo;


switch_status_t PDT_API_Heartbeat(uint16_t AgentNo)
{
	PDTDATA_HeartBeat struHeartBeat;
	int nLen = sizeof(struHeartBeat);
	memset(&struHeartBeat, 0, nLen);
	struHeartBeat.AgentNo = AgentNo;
	struHeartBeat.AgentStatus = 1;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "PDT_API_Heartbeat(%d)\n", AgentNo);
	return PDT_Data_Send(DATA_ID_HeartBeat, (void*)&struHeartBeat, nLen);
}

switch_status_t PDT_API_Makecall(uint16_t AgentNo, uint32_t CalledSSI, uint32_t CallerSSI, uint8_t IsSimulate, uint16_t CallType, uint16_t CallTime, uint8_t Conv)
{
	PDTDATA_Makecall struMakecall;
	int nLen = PDTData_InitWithAgentNo(struMakecall, AgentNo);
	struMakecall.CalledSSI = CalledSSI;
	struMakecall.CallerSSI = CallerSSI;
	struMakecall.IsSimulate = IsSimulate;
	struMakecall.CallType = CallType;
	struMakecall.CallTime = CallTime;
	struMakecall.Conv = Conv;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT_API_Makecall(%d) CallerSSI:%d,CalledSSI:%d,CallType:%d\n", AgentNo, CallerSSI, CalledSSI, CallType);
	return PDT_Data_Send(DATA_ID_Makecall, (void*)&struMakecall, nLen);

}

switch_status_t PDT_API_MakecallAck(uint16_t AgentNo, uint32_t Mid, uint32_t CalledSSI, uint32_t CallerSSI, uint8_t Opt)
{
	PDTDATA_MakecallAck struMakecallAck;
	int nLen = PDTData_InitWithAgentNo(struMakecallAck, AgentNo);

	struMakecallAck.Mid = Mid;
	struMakecallAck.CalledSSI = CalledSSI;
	struMakecallAck.CallerSSI = CallerSSI;
	struMakecallAck.Opt = Opt;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
		"PDT_API_MakecallAck AgentNo:%d,Mid:%d,CalledSSI:%d,CallerSSI:%d,Opt:%d\n"
		, AgentNo, Mid, CalledSSI, CallerSSI, Opt);
	return PDT_Data_Send(DATA_ID_MakecallAck, (void*)&struMakecallAck, nLen);
}

switch_status_t PDT_API_Quit(uint16_t AgentNo, uint32_t Mid, uint8_t ChanNum, uint8_t ReqFrom, uint8_t Opt)
{
	PDTDATA_Quit struQuit;
	int nLen = PDTData_InitWithAgentNo(struQuit, AgentNo);
	struQuit.Mid = Mid;
	struQuit.ChanNum = ChanNum;
	struQuit.ReqFrom = ReqFrom;
	struQuit.Opt = Opt;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT_API_Quit(%d) Mid:%d\n", AgentNo, Mid);
	return PDT_Data_Send(DATA_ID_Quit, (void*)&struQuit, nLen);
}

switch_status_t PDT_API_PTT(uint16_t AgentNo, uint32_t Mid, uint32_t CallerSSI, uint8_t Opt)
{
	PDTDATA_PTT struPTT;
	int nLen = PDTData_InitWithAgentNo(struPTT, AgentNo);
	struPTT.Mid = Mid;
	struPTT.CallerSSI = CallerSSI;
	struPTT.Opt = Opt;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT_API_PTT(%d) Mid:%d,CallerSSI:%d,Opt:%d\n", AgentNo, Mid, CallerSSI, Opt);
	return PDT_Data_Send(DATA_ID_PTT, (void*)&struPTT, nLen);
}

switch_status_t PDT_API_SMS(uint16_t AgentNo, uint32_t CalledSSI, uint32_t CallerSSI, uint16_t Ctype, char* ContentOri, uint16_t ContentOriLen)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	PDTDATA_SMS struSMS;
	int nSendLen = 0;
	char* cSendData = NULL;
	char Content[2048];
	switch_size_t ContentLen = 0;
	switch_size_t OutputLeftLen = 0;
	switch_size_t OutputLen = sizeof(Content);
	int nLen = PDTData_InitWithAgentNo(struSMS, AgentNo);
	if (CallerSSI == 0)
	{
		CallerSSI = pdt_globals.CallerSSI;
	}
	struSMS.CalledSSI = CalledSSI;
	struSMS.CallerSSI = CallerSSI;
	struSMS.Ctype = Ctype;
	memset(Content, '\0', sizeof(Content));

	OutputLeftLen = trans("UTF-8", "UTF-16", ContentOri, ContentOriLen, Content, OutputLen);
	ContentLen = OutputLen - OutputLeftLen;
// 	memcpy(Content, ContentOri, ContentOriLen);
// 	ContentLen = ContentOriLen;

	nSendLen = nLen + ContentLen;
	if (nSendLen > nLen)
	{
		cSendData = (char*)malloc(nSendLen);
		memcpy(cSendData, &struSMS, nLen);
		memcpy(cSendData + nLen, Content + 2, ContentLen);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO,
		"PDT_API_SMS CalledSSI:%d,CallerSSI:%d,Ctype:%d,SMS_ori[%d]:%s,SMS[%d]:%s\n"
		, CalledSSI	, CallerSSI	, Ctype, ContentOriLen, ContentOri, ContentLen	, Content);


	status = PDT_Data_Send(DATA_ID_SMS, (void*)cSendData, nSendLen);
	free(cSendData);
	return status;
}

switch_status_t PDT_API_SMS_Ack(uint16_t AgentNo, uint32_t CalledSSI, uint32_t CallerSSI, uint16_t Ctype, uint8_t Ret)
{
	PDTDATA_SMS_Ack struSMSAck;
	int nLen = PDTData_InitWithAgentNo(struSMSAck, AgentNo);
	struSMSAck.CalledSSI = CalledSSI;
	struSMSAck.CallerSSI = CallerSSI;
	struSMSAck.Ctype = Ctype;
	struSMSAck.Ret = Ret;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT_API_SMS_Ack(%d)\n", AgentNo);
	return PDT_Data_Send(DATA_ID_SMS_Ack, (void*)&struSMSAck, nLen);
}

switch_status_t PDT_API_MANAGE(uint16_t AgentNo, uint32_t RadioSSI, uint16_t Opt)
{
	PDTDATA_MANAGE struManage;
	if (AgentNo == 0)
	{
		AgentNo = pdt_globals.AgentNo;
	}
	int nLen = PDTData_InitWithAgentNo(struManage, AgentNo);
	struManage.RadioSSI = RadioSSI;
	struManage.Opt = Opt;
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT_API_MANAGE(%d) RadioSSI:%d,Opt:%d\n", AgentNo, RadioSSI, Opt);
	return PDT_Data_Send(DATA_ID_MANAGE, (void*)&struManage, nLen);

}
