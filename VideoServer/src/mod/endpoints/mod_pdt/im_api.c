#include "mod_pdt.h"

#include "im_api.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>

extern globals_t pdt_globals;
char * GetHttpResponse(char* params) {
	char* paramsCurl = NULL;
	char *cResponse = NULL;
	switch_stream_handle_t stream = { 0 };
	SWITCH_STANDARD_STREAM(stream);
	paramsCurl = switch_mprintf("%s connect-timeout 3 timeout 2", params);
	if (switch_api_execute("curl", paramsCurl, NULL, &stream) == SWITCH_STATUS_SUCCESS && stream.data) {
		cResponse = switch_core_strdup(pdt_globals.pool, (char*)stream.data);
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "GetHttpResponse cResponse:%s\n", switch_str_nil(cResponse));
	}
	if (stream.data)
	{
		switch_safe_free(stream.data);
	}
	switch_safe_free(paramsCurl);
	return cResponse;
}
//ppsEntity对象使用后，需要调用cJSON_Delete释放
switch_status_t CheckResponse(char *cResponse, cJSON** ppsEntity) {
	switch_status_t status = SWITCH_STATUS_FALSE;
	cJSON *response = NULL;
	if (cResponse && strlen(cResponse) > 0)
	{
		response = cJSON_Parse(cResponse);
		if (response != NULL)
		{
			cJSON* jsonSuccess = cJSON_GetObjectItem(response, "IsSuccess");
			if (jsonSuccess != NULL && jsonSuccess->type == cJSON_True) {
				status = SWITCH_STATUS_SUCCESS;
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "CheckResponse Success\n");
				if (ppsEntity != NULL)
				{
					cJSON* Entity = cJSON_GetObjectItem(response, "Entity");
					if (Entity != NULL)
					{
						*ppsEntity = cJSON_Duplicate(Entity, 1);
					}
				}
			}
			else {
				char Content[2048] = { 0 };
				int OutputLeftLen = 0;
				int OutputLen = sizeof(Content);
				memset(Content, '\0', sizeof(Content));
				OutputLeftLen = trans("UTF-8", "GBK", cResponse, strlen(cResponse) + 1, Content, OutputLen);
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "CheckResponse IsSuccess: false Response:%s\n", Content);
			}
			if (response) {
				cJSON_Delete(response);
			}
		}

	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "cResponse Is Null\n");
	}
	return status;
}
// switch_status_t CheckResponse(char *cResponse) {
// 	return CheckResponse(cResponse, NULL);
// }
switch_status_t IM_Login() 
{
	switch_status_t status = SWITCH_STATUS_FALSE;
//	switch_stream_handle_t stream = { 0 };
	char* params = NULL;
	cJSON *Entity = NULL;
//	char *cResponse = NULL;
	char pswMd5[SWITCH_MD5_DIGEST_STRING_SIZE] = "";
	char* pswMd5Upper = NULL;
// 	char pswMd5[SWITCH_MD5_DIGEST_STRING_SIZE] = "06E6C0EA3096DA10229E4B8D9612BAC3";
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_Login %s %s\n", pdt_globals.im_LoginName, pdt_globals.im_UserPwd);
	switch_md5_string(pswMd5, (void *)pdt_globals.im_UserPwd, strlen(pdt_globals.im_UserPwd));
//	pswMd5Upper = switch_uc_strdup(pswMd5);
	pswMd5Upper = pdt_globals.im_UserPwd;

	params = switch_mprintf("%s/api/do/userLogin post LoginName=%s&UserPwd=%s",
		pdt_globals.im_ServerUrl,pdt_globals.im_LoginName, pswMd5Upper);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_Login params:%s\n", params);

	if (CheckResponse(GetHttpResponse(params), &Entity) == SWITCH_STATUS_SUCCESS) {
		//returnCode = switch_core_strdup(pdt_globals.pool, cResponse);
		if (Entity != NULL)
		{
// 			const char* fGetCode = Entity->type == cJSON_String ? Entity->valuestring : "";//cJSON_Print(Entity);
// 			if (fGetCode != NULL)
// 			{
// 				returnCode = switch_core_strdup(pdt_globals.pool, fGetCode);
// 			}
			const char* ApiToken = NULL;
			ApiToken = cJSON_GetObjectCstr(Entity, "ApiToken");
			if (ApiToken != NULL)
			{
				status = SWITCH_STATUS_SUCCESS;
				pdt_globals.im_ApiToken = switch_core_strdup(pdt_globals.pool, ApiToken);
				pdt_globals.bImConnected = SWITCH_TRUE;
			}
			const char* UserCode = NULL;
			UserCode = cJSON_GetObjectCstr(Entity, "UserCode");
			if (UserCode != NULL)
			{
				pdt_globals.im_UserCode = switch_core_strdup(pdt_globals.pool, UserCode);
			}
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_Login Success ApiToken:%s,UserCode:%s\n", switch_str_nil(ApiToken), switch_str_nil(UserCode));


			cJSON_Delete(Entity);
		}
	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "get fail\n");
	}
// 	SWITCH_STANDARD_STREAM(stream);
// 	if (switch_api_execute("curl", params, NULL, &stream) == SWITCH_STATUS_SUCCESS && stream.data) {
// 		cResponse = (char*)stream.data;
// 		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_Login cResponse:%s\n", switch_str_nil(cResponse));
// 		if (cResponse && strlen(cResponse) > 0)
// 		{
// 			response = cJSON_Parse(cResponse);
// 			if (response != NULL)
// 			{
// 				cJSON* jsonSuccess = cJSON_GetObjectItem(response, "IsSuccess");
// 				if (jsonSuccess != NULL && jsonSuccess->type == cJSON_True) {
// 					cJSON* Entity = cJSON_GetObjectItem(response, "Entity");
// 					if (Entity != NULL)
// 					{
// 						const char* ApiToken = NULL;
// 						ApiToken = cJSON_GetObjectCstr(Entity, "ApiToken");
// 						if (ApiToken != NULL)
// 						{
// 							status = SWITCH_STATUS_SUCCESS;
// 							pdt_globals.im_ApiToken = switch_core_strdup(pdt_globals.pool, ApiToken);
// 						}
// 						const char* UserCode = NULL;
// 						UserCode = cJSON_GetObjectCstr(Entity, "UserCode");
// 						if (UserCode != NULL)
// 						{
// 							pdt_globals.im_UserCode = switch_core_strdup(pdt_globals.pool, UserCode);
// 						}
// 						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_Login Success ApiToken:%s,UserCode:%s\n", switch_str_nil(ApiToken), switch_str_nil(UserCode));
// 					}
// 				}
// 				else {
// 					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM_Login IsSuccess: false cResponse:%s\n", cResponse);
// 				}
// 				cJSON_Delete(response);
// 			} else {
// 				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "Get response json Error cResponse:%s\n", cResponse);
// 			}
// 
// 
// 		}
// 		else {
// 			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM_Login false\n");
// 		}



// 		switch_safe_free(stream.data);
// 	}

	//switch_safe_free(url);
//	switch_safe_free(params);
// 	if (status != SWITCH_STATUS_SUCCESS)
// 	{
// 		pdt_globals.bImConnected = SWITCH_FALSE;
// 	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_Login status:%d\n", status);
	return status;
}

char* IM_Postfile(char* file)
{
	switch_stream_handle_t stream = { 0 };
	char* fCode = NULL;
	char* params = NULL;
	cJSON *response = NULL;
	char *cResponse = NULL;
	if (switch_file_exists(file,pdt_globals.pool)!= SWITCH_STATUS_SUCCESS)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_Postfile Not Exist:%s\n", file);
		return NULL;
	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_Postfile file:%s\n", file);
	params = switch_mprintf("%s/api/do/PostFile filename_post_name=%s nopost stream",
		pdt_globals.im_ServerUrl, file);

	SWITCH_STANDARD_STREAM(stream);
	if (switch_api_execute("curl_sendfile", params, NULL, &stream) == SWITCH_STATUS_SUCCESS && stream.data) {
		cResponse = strchr((char*)stream.data, '\n');//去掉第一行 200 OK的文本
		//switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_Postfile cResponse:%s\n", switch_str_nil(cResponse));
		if (cResponse && strlen(cResponse) > 0)
		{
			response = cJSON_Parse(cResponse);
			if (response != NULL)
			{
				cJSON* jsonSuccess = cJSON_GetObjectItem(response, "IsSuccess");
				if (jsonSuccess && jsonSuccess->type == cJSON_True) {
					cJSON* Entity = cJSON_GetObjectItem(response, "Entity");
					if (Entity != NULL)
					{
						const char* fGetCode = NULL;
						fGetCode = cJSON_GetObjectCstr(Entity, "fCode");
						if (fGetCode != NULL)
						{
							fCode = switch_core_strdup(pdt_globals.pool, fGetCode);
						}
					}
				}
				else {
					char Content[2048] = { 0 };
					int OutputLeftLen = 0;
					int OutputLen = sizeof(Content);
					memset(Content, '\0', sizeof(Content));
					OutputLeftLen = trans("UTF-8", "GBK", cResponse, strlen(cResponse) + 1, Content, OutputLen);
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "curl_sendfile IsSuccess: false Response:%s\n", Content);
				}
			}

		}
		else {
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "curl_sendfile false\n");
		}
		switch_safe_free(stream.data);
	}
	//switch_safe_free(url);
	switch_safe_free(params);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Upload fCode:%s\n", switch_str_nil(fCode));
	return fCode;
}

uint32_t IM_getPdtGroupID(char* AppGroupId)
{
	char* cPdtCode = IM_getPdtAndAppGroup(AppGroupId, SWITCH_TRUE);
	return switch_safe_atoi(cPdtCode, 0);
}


char* IM_getAppGroupID(uint32_t pdtSSI)
{
	char sCallerSSI[100] = { 0 };
	switch_snprintf(sCallerSSI, 100, "%d", pdtSSI);
	return IM_getPdtAndAppGroup(sCallerSSI, SWITCH_FALSE);;
}

char* IM_getPdtAndAppGroup(char* code,switch_bool_t bGetPdt)
{
	char* returnCode = NULL;
	char* params = NULL;
	char *getType = NULL;
	cJSON* Entity = NULL;
	if (code == NULL || strlen(code) == 0)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM_getPdtAndAppGroup code is empty\n");
		return NULL;
	}
	if (bGetPdt == SWITCH_TRUE)	{
		getType = "getPdtGroup";
	} else {
		getType = "getAPPGroup";
	}
	params = switch_mprintf("%s/api/do/%s post ApiToken=%s&UserCode=%s&DisscusionCode=%s",
		pdt_globals.im_ServerUrl, getType, pdt_globals.im_ApiToken, pdt_globals.im_UserCode, switch_str_nil(code));
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_getPdtAndAppGroup params:%s\n", params);
	if (CheckResponse(GetHttpResponse(params),&Entity) == SWITCH_STATUS_SUCCESS) {
		//returnCode = switch_core_strdup(pdt_globals.pool, cResponse);
		if (Entity != NULL)
		{
			const char* fGetCode = Entity->type == cJSON_String?Entity->valuestring:"";//cJSON_Print(Entity);
			if (fGetCode != NULL)
			{
				returnCode = switch_core_strdup(pdt_globals.pool, fGetCode);
			}
			cJSON_Delete(Entity);
		}
	} else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "get fail\n");
	}

	switch_safe_free(params);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "returnCode:%s\n", switch_str_nil(returnCode));
	return returnCode;
}

switch_status_t IM_pushMsg(const char* MsgFromType, const char* MsgToCode, const char* MsgContent,const char* MsgFile, const char* SenderUserCode)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	char* params = NULL;
	char *cResponse = NULL;
	char *MsgType = NULL;

	char Content[2048] = { 0 };
	int OutputLeftLen = 0;
	int OutputLen = sizeof(Content);
	if (MsgContent == NULL || strlen(MsgContent) == 0)
	{
		return status;
	}
	memset(Content, '\0', sizeof(Content));
	OutputLeftLen = trans("GBK", "UTF-8", MsgContent, strlen(MsgContent)+1, Content, OutputLen);


	if (MsgFile != NULL)
	{
		MsgType = "Audio";
	}
	else {
		MsgType = "Text";
	}
	if (SenderUserCode == NULL)
	{
		SenderUserCode = pdt_globals.im_UserCode;
	}

	params = switch_mprintf("%s/api/do/pushMsg post ApiToken=%s&UserCode=%s&MsgFromType=%s&MsgToCode=%s&MsgType=%s&MsgContent=%s&MsgFile=%s",
		pdt_globals.im_ServerUrl,pdt_globals.im_ApiToken, SenderUserCode, MsgFromType, MsgToCode, MsgType, switch_str_nil(Content), switch_str_nil(MsgFile));
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushMsg params:%s\n", params);
	if (params) {
		cResponse = GetHttpResponse(params);
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_pushMsg cResponse:%s\n", switch_str_nil(cResponse));
		status = CheckResponse(cResponse,NULL);
		//switch_safe_free(cResponse);

		switch_safe_free(params);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_pushMsg status:%d\n", status);
	return status;
}
switch_status_t IM_pushRecordingWithFile(char *file, char* MsgToCode, char* UserCode, const char* cMulticast, const char *cSeconds,const char* conference_uuid)
{
	switch_status_t status = SWITCH_STATUS_SUCCESS;
	char* fCode = IM_Postfile(file);
	if (fCode)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushRecordingWithFile MsgToCode:%s,UserCode:%scMulticast:%s fCode:%s conference_uuid:%s!\n", MsgToCode, UserCode, cMulticast, fCode, conference_uuid);
		const char* cGroupType = "";

		if (!strcasecmp(cMulticast, "1")) {
			cGroupType = "Group";
		}
		else {
			cGroupType = "Person";
		}

		status = IM_pushRecording(cGroupType, UserCode, MsgToCode, fCode, switch_safe_atoi(cSeconds, 0),conference_uuid);
		//switch_status_t status = IM_pushMsg("Group", conference_name,NULL, fCode);
	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM_pushRecordingWithFile IM_Postfile Fail!\n");
		status = SWITCH_STATUS_FALSE;
	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushRecording status:%d!\n", status);
	return status;
}
switch_status_t IM_pushRecording(const char* MsgFromType, const char* UserCode, const char* MsgToCode, const char* MsgFile,int Second, const char* conference_uuid)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	char* params = NULL;
	char *cResponse = NULL;
	char *MsgType = "Recording";;

	if (strlen(switch_str_nil(UserCode)) == 0)
	{
		UserCode = pdt_globals.im_UserCode;
	}
	params = switch_mprintf("%s/api/do/pushRecording post ApiToken=%s&UserCode=%s&MsgFromType=%s&MsgToCode=%s&MsgType=%s&MsgFile=%s&Second=%d&virtualId=%s",
		pdt_globals.im_ServerUrl, pdt_globals.im_ApiToken, UserCode, MsgFromType, MsgToCode, MsgType, switch_str_nil(MsgFile), switch_str_nil(Second), switch_str_nil(conference_uuid));
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushRecording params:%s\n", params);
	if (params) {
		cResponse = GetHttpResponse(params);
	//	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_pushRecording cResponse:%s\n", switch_str_nil(cResponse));
		status = CheckResponse(cResponse,NULL);
		//switch_safe_free(cResponse);

		switch_safe_free(params);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_pushRecording status:%d\n", status);
	return status;
}
switch_status_t IM_pushCommand(const char* MsgFromType, const char* MsgToCode, const char* MsgContent)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	char* params = NULL;
	char *cResponse = NULL;
	char *MsgType = "Command";

	params = switch_mprintf("%s/api/do/pushCommand post ApiToken=%s&UserCode=%s&MsgFromType=%s&MsgToCode=%s&MsgType=%s&MsgContent=%s",
		pdt_globals.im_ServerUrl, pdt_globals.im_ApiToken, pdt_globals.im_UserCode, MsgFromType, MsgToCode, MsgType, switch_str_nil(MsgContent));
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushCommand params:%s\n", params);
	if (params) {
		cResponse = GetHttpResponse(params);
		status = CheckResponse(cResponse, NULL);
		switch_safe_free(params);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_pushCommand status:%d\n", status);
	return status;
}
switch_status_t IM_pushEvent_Base(const char* MsgFromType, char *appGroupID, const char* cEventName, cJSON* obj)
{
//	cJSON *obj = cJSON_CreateObject();
	switch_status_t status = SWITCH_STATUS_FALSE;
	switch_bool_t bJsonNeedDelete = SWITCH_FALSE;
	if (obj == NULL)
	{
		obj = cJSON_CreateObject();
		bJsonNeedDelete = SWITCH_TRUE;
	}
	cJSON_AddItemToObject(obj, "EventName", cJSON_CreateString(cEventName));

 	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushEvent MsgFromType:%s,cEventName:%s!\n", MsgFromType,cEventName);
	status = IM_pushCommand(MsgFromType, appGroupID, cJSON_PrintUnformatted(obj));
 	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushEvent status:%d!\n", status);

	if (bJsonNeedDelete)
	{
		cJSON_Delete(obj);
	}
	return status;
}
// switch_status_t IM_pushEvent_Group(char *appGroupID, const char* cEventName, cJSON* obj)
// {
// 	return IM_pushEvent_Base("Group", appGroupID, cEventName, obj);
// }
// switch_status_t IM_pushEvent_Person(char *appUserCode, const char* cEventName, cJSON* obj)
// {
// 	return IM_pushEvent_Base("Person", appUserCode, cEventName, obj);
// }


switch_bool_t Fs_cmd_pdt(const char* cmd, const char* arg, char** pcGetResult)
{
	switch_bool_t bReturn = SWITCH_FALSE;
	switch_stream_handle_t stream = { 0 };
	SWITCH_STANDARD_STREAM(stream);

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Fs_cmd_pdt %s %s\n", cmd, arg);
	if (switch_api_execute(cmd, arg, NULL, &stream) == SWITCH_STATUS_SUCCESS) {
		char* sResult = (char*)stream.data;
		if (pcGetResult != NULL && sResult != NULL)
		{
			*pcGetResult = switch_core_strdup(pdt_globals.pool, sResult);
			bReturn = SWITCH_TRUE;
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Fs_cmd_pdt Get sResult:%s\n", sResult);
		}
		else {
			if (switch_strstr(switch_str_nil(sResult), "OK"))
			{
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Fs_cmd_pdt OK sResult:%s\n", sResult);
				bReturn = SWITCH_TRUE;
			}
			else {
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Fs_cmd_pdt ERR sResult:%s\n", sResult);
			}
		}

	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Fs_cmd_pdt switch_api_execute failed\n");
	}
	switch_safe_free(stream.data);
	return bReturn;
}

switch_bool_t IM_IsPdtSSI(uint32_t pdtSSI)
{
// 	switch_bool_t bReturn = SWITCH_FALSE;
// 	char cmd[512] = { 0 };
// 	char *GetResult = NULL;
// 	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_IsPdtSSI pdtSSI:%d\n", pdtSSI);
// 
// 	switch_snprintf(cmd, sizeof(cmd), "id %d %s", pdtSSI, pdt_globals.domain_name);
// 	if (Fs_cmd_pdt("user_exists", cmd, &GetResult))
// 	{
// 		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "cmd:%s GetResult:%s\n", cmd, GetResult);
// 		if (switch_strstr(switch_str_nil(GetResult), "true"))
// 		{
// 			bReturn = SWITCH_TRUE;
// 		}
// 	}
// 	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_IsPdtSSI bReturn:%d\n", bReturn);
// 	return bReturn;
	char sPdtSSI[100] = { 0 };
	switch_snprintf(sPdtSSI, 100, "%d", pdtSSI);

	return IM_getAppUserID(sPdtSSI, GET_Pdt) != NULL;
}

switch_bool_t IM_IsAppSSI(uint32_t pdtSSI)
{
	char sPdtSSI[100] = { 0 };
	switch_snprintf(sPdtSSI, 100, "%d", pdtSSI);
	return IM_getAppUserID(sPdtSSI,GET_App) != NULL;
}

struct user_struct {
	char *dname;
	char *gname;
	char *effective_caller_id_name;
	char *effective_caller_id_number;
	char *callgroup;
	switch_xml_t x_user_tag;
	switch_stream_handle_t *stream;
	char *search_context;
	char *context;
	switch_xml_t x_domain_tag;
};

// char* IM_getAppUserID(char* code)
// {
// 	char* returnCode = NULL;
// 	char* params = NULL;
// 	char *getType = NULL;
// 	cJSON* Entity = NULL;
// 	if (code == NULL || strlen(code) == 0)
// 	{
// 		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM_getPdtAndAppGroup code is empty\n");
// 		return NULL;
// 	}
// 	getType = "getUserByDeviceId";
// 
// 	params = switch_mprintf("%s/api/do/%s post ApiToken=%s&UserCode=%s&DeviceId=%s",
// 		pdt_globals.im_ServerUrl, getType, pdt_globals.im_ApiToken, pdt_globals.im_UserCode, switch_str_nil(code));
// 	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_getAppUserID params:%s\n", params);
// 	if (CheckResponse(GetHttpResponse(params), &Entity) == SWITCH_STATUS_SUCCESS) {
// 		//returnCode = switch_core_strdup(pdt_globals.pool, cResponse);
// 		if (Entity != NULL)
// 		{
// 			const char* fGetCode = Entity->type == cJSON_String ? Entity->valuestring : "";//cJSON_Print(Entity);
// 			if (fGetCode != NULL)
// 			{
// 				returnCode = switch_core_strdup(pdt_globals.pool, fGetCode);
// 			}
// 			cJSON_Delete(Entity);
// 		}
// 	}
// 	else {
// 		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "get fail\n");
// 	}
// 
// 	switch_safe_free(params);
// 	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "returnCode:%s\n", switch_str_nil(returnCode));
// 	return returnCode;
// }
// char* IM_getAppUserID_SSI(uint32_t pdtSSI)
// {
// 	char sCallerSSI[100] = { 0 };
// 	switch_snprintf(sCallerSSI, 100, "%d", pdtSSI);
// 	return IM_getAppUserID(sCallerSSI);
// }
char* IM_getAppUserID_SSI(uint32_t pdtSSI, ENUM_GET_TYPE GetType)
{
	char sCallerSSI[100] = { 0 };
	switch_snprintf(sCallerSSI, 100, "%d", pdtSSI);
	return IM_getAppUserID(sCallerSSI, GetType);
}
char* IM_getAppUserID_XML(char* cPdtSSI, ENUM_GET_TYPE GetType)
{
	switch_bool_t bReturn = SWITCH_FALSE;
	switch_xml_t xml_root, x_domains, x_domain_tag;
	switch_xml_t gts, gt, uts, ut;
	char * GetCode = NULL;
	char *_domain = NULL;
	char * var;
	char *_user = NULL, *_search_context = NULL, *_group = NULL, *section = "directory";
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_getAppUserID cPdtSSI:%s GetType:%d!\n", switch_str_nil(cPdtSSI), GetType);
	if (switch_xml_locate(section, NULL, NULL, NULL, &xml_root, &x_domains, NULL, SWITCH_FALSE) == SWITCH_STATUS_SUCCESS) {
		struct user_struct us = { 0 };

		for (x_domain_tag = _domain ? x_domains : switch_xml_child(x_domains, "domain"); x_domain_tag; x_domain_tag = x_domain_tag->next) {
			switch_xml_t x_vars, x_var;
			us.dname = (char*)switch_xml_attr_soft(x_domain_tag, "name");
			if (_domain && strcasecmp(_domain, us.dname)) {
				continue;
			}
			if ((gts = switch_xml_child(x_domain_tag, "groups"))) {
				for (gt = switch_xml_child(gts, "group"); gt; gt = gt->next) {
					us.gname = (char*)switch_xml_attr_soft(gt, "name");
					if (_group && strcasecmp(_group, us.gname)) {
						continue;
					}
					for (uts = switch_xml_child(gt, "users"); uts; uts = uts->next) {
						for (ut = switch_xml_child(uts, "user"); ut; ut = ut->next) {
							switch_bool_t bGetSSI = SWITCH_FALSE;
							switch_bool_t bCheckOk = SWITCH_FALSE;

							if (_user && strcasecmp(_user, switch_xml_attr_soft(ut, "id"))) {
								continue;
							}

							char* uname = (char *)switch_xml_attr_soft(ut, "id");
							if ((x_vars = switch_xml_child(ut, "params"))) {
								for (x_var = switch_xml_child(x_vars, "param"); x_var; x_var = x_var->next) {
									const char *key = switch_xml_attr_soft(x_var, "name");
									const char *val = switch_xml_attr_soft(x_var, "value");

									if (!strcasecmp(key, "pdt-deviceid")) {
										if (!strcasecmp(val, cPdtSSI)) {
											bGetSSI = SWITCH_TRUE;
											if (GetType == GET_All)
											{//获取所有时，pdtSSI匹配到即可退出循环
												bCheckOk = SWITCH_TRUE;
												break;
											}
										}
										//user_context = (char*)val;
									}
									else if (!strcasecmp(key, "type")) {

										if (GetType == GET_Pdt)
										{
											if (!strcasecmp(val, "PDT")) {
												bCheckOk = SWITCH_TRUE;
											}
										}
										else if(GetType == GET_App)
										{
											if (!strcasecmp(val, "APP")) {
												bCheckOk = SWITCH_TRUE;
											}
										}

									}

								}
							}
							if (bGetSSI && bCheckOk)
							{
								GetCode = switch_core_strdup(pdt_globals.pool, uname);
								goto end;
							}
						}
					}
				}
			}

		}
	}
end:
	if (xml_root)
	{
		switch_xml_free(xml_root);
	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_getAppUserID GetCode:%s!\n", switch_str_nil(GetCode));
	return GetCode;
}
char* IM_getAppUserID_Http(char* cPdtSSI, ENUM_GET_TYPE GetType)
{
	char* returnCode = NULL;
	char* params = NULL;
	char *getType = NULL;
	cJSON* Entity = NULL;
	if (cPdtSSI == NULL || strlen(cPdtSSI) == 0)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "IM_getAppUserID_Http cPdtSSI is empty\n");
		return NULL;
	}

	getType = "getAppUserID";

	params = switch_mprintf("%s/api/do/%s post ApiToken=%s&UserCode=%s&pdtSSI=%s&type=%d",
		pdt_globals.im_ServerUrl, getType, pdt_globals.im_ApiToken, pdt_globals.im_UserCode, switch_str_nil(cPdtSSI), GetType);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_getAppUserID_Http params:%s\n", params);
	if (CheckResponse(GetHttpResponse(params), &Entity) == SWITCH_STATUS_SUCCESS) {
		//returnCode = switch_core_strdup(pdt_globals.pool, cResponse);
		if (Entity != NULL)
		{
			const char* fGetCode = Entity->type == cJSON_String ? Entity->valuestring : "";//cJSON_Print(Entity);
			if (fGetCode != NULL)
			{
				returnCode = switch_core_strdup(pdt_globals.pool, fGetCode);
			}
			cJSON_Delete(Entity);
		}
	}
	else {
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "get fail\n");
	}

	switch_safe_free(params);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "returnCode:%s\n", switch_str_nil(returnCode));
	return returnCode;
}
char* IM_getAppUserID(char* cPdtSSI, ENUM_GET_TYPE GetType)
{
//	return IM_getAppUserID_XML(cPdtSSI, GetType);
	return IM_getAppUserID_Http(cPdtSSI, GetType);
}
switch_status_t IM_getIMStatus()
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	char* params = NULL;
	char *cResponse = NULL;
	params = switch_mprintf("%s/api/do/getIMStatus post ApiToken=%s",
		pdt_globals.im_ServerUrl, pdt_globals.im_ApiToken);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_getIMStatus params:%s\n", params);
	if (params) {
		cResponse = GetHttpResponse(params);
		status = CheckResponse(cResponse, NULL);
		switch_safe_free(params);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_DEBUG, "IM_getIMStatus status:%d\n", status);
	return status;
}