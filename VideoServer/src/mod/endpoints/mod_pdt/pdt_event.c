#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "mod_pdt.h"
#include "pdt_api.h"
#include "im_api.h"

extern globals_t pdt_globals;
uint32_t g_nEventListLastId = 0;
void Pdt_SendEvent_PTT(uint32_t Mid, int nPttState, uint32_t CallerSSI, uint32_t CalledSSI)
{
	switch_event_t *event = NULL;
	const char* action = "from-pdt-ptt";

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt SendEvent_PTT action:%s\n", action);
	if (switch_event_create_subclass(&event, SWITCH_EVENT_CUSTOM, PDT_EVENT_MAINT) == SWITCH_STATUS_SUCCESS) {
		switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "Action", action);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "Mid", "%d", Mid);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "ptt_type","%d", nPttState);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "CallerSSI", "%d", CallerSSI);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "CalledSSI", "%d", CalledSSI);
		switch_event_fire(&event);
	}
}
void Pdt_SendEvent_Quit(uint32_t Mid, uint8_t ChanNum, uint8_t ReqFrom, uint8_t Opt, uint32_t CallerSSI)
{
	switch_event_t *event;
	char* action = "pdt-quit";

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt SendEvent action:%s\n", action);
	if (switch_event_create_subclass(&event, SWITCH_EVENT_CUSTOM, PDT_EVENT_MAINT) == SWITCH_STATUS_SUCCESS) {
		switch_event_add_header_string(event, SWITCH_STACK_BOTTOM, "Action", action);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "Mid", "%d", Mid);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "ChanNum", "%d", ChanNum);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "ReqFrom", "%d", ReqFrom);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "Opt", "%d", Opt);
		switch_event_add_header(event, SWITCH_STACK_BOTTOM, "CallerSSI", "%d", CallerSSI);
		switch_event_fire(&event);
	}

}

void event_handler_ptt(switch_event_t *event, int opt) {
//	uint32_t CallerSSI = pdt_globals.CallerSSI;
	char * cCallerSSI = switch_event_get_header(event, "CallerSSI");
	char * cCalledSSI = switch_event_get_header(event, "CalledSSI");
	uint32_t CallerSSI = switch_safe_atoi(cCallerSSI, 0);
	if (cCalledSSI == NULL)
	{
		cCalledSSI = switch_event_get_header(event, "conference-name");
	}
	if (CallerSSI == 0)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Ptt no CallerSSI use pdt_globals.CallerSSI:%d!\n", pdt_globals.CallerSSI);
		CallerSSI = pdt_globals.CallerSSI;
	}
	char *cMid = switch_event_get_header(event, "mid");
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "event PTT opt:%d cMid:%s,CallerSSI:%d!\n", opt, cMid, CallerSSI);
	switch_status_t status = PDT_API_PTT(pdt_globals.AgentNo, switch_safe_atoi(cMid, 0), CallerSSI, (uint8_t)opt);
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "event PTT opt:%d status:%d!\n", status, opt);
	
}


ENUM_CALL_TYPE ToCallType(char *cMulticast) {
	return switch_safe_atoi(cMulticast, 0);
}

switch_status_t event_handler_IM_event(switch_event_t *event) {
	switch_status_t status = SWITCH_STATUS_FALSE;
	//	uint32_t CallerSSI = pdt_globals.CallerSSI;
	char * cEventName = switch_event_get_header(event, "EventName");
	cJSON* obj = cJSON_CreateObject();
	char *cMulticast = switch_event_get_header(event, "Multicast");
	char * cCalledSSI = switch_event_get_header(event, "CalledSSI");
	char * cCallerSSI = switch_event_get_header(event, "CallerSSI");
	const char* cGroupType = NULL;
	char * MsgToCode = NULL;
	if (cCalledSSI == NULL)
	{
		cCalledSSI = switch_event_get_header(event, "conference-name");
	}
	if (ToCallType(cMulticast) == CALL_Type_Multicast) {
		cGroupType = "Group";
		MsgToCode = IM_getPdtAndAppGroup(cCalledSSI, SWITCH_FALSE);
	}
	else {
		cGroupType = "Person";
		//单呼时
		MsgToCode = IM_getAppUserID(cCalledSSI, GET_All);
	}

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "event_handler_IM_event:%s cCalledSSI:%s,cMulticast:%s,cGroupType:%s,MsgToCode:%s\n",
		switch_str_nil(cEventName), switch_str_nil(cCalledSSI), switch_str_nil(cMulticast), switch_str_nil(cGroupType), switch_str_nil(MsgToCode));
	if (!strcasecmp(cEventName, "conference_ptt")) {
		char * ptt_type = switch_event_get_header(event, "ptt_type");
		char * IsPdt = switch_event_get_header(event, "IsPdt");
		uint32_t CallerSSI = switch_safe_atoi(cCallerSSI, 0);
		if (CallerSSI == 0)
		{
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Ptt no CallerSSI use pdt_globals.CallerSSI:%d!\n", pdt_globals.CallerSSI);
			CallerSSI = pdt_globals.CallerSSI;
		}
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "conference_ptt ptt_type:%s!\n", ptt_type);

		if (obj)
		{
			cJSON_AddStringToObject(obj, "IsPdt", IsPdt);
			cJSON_AddStringToObject(obj, "ptt_type", ptt_type);
			cJSON_AddStringToObject(obj, "CalledSSI", cCalledSSI);
		}

	}

	char *conference_Creator = switch_event_get_header(event, "conference_Creator");
	char *conference_CreatorIsPdt = switch_event_get_header(event, "conference_CreatorIsPdt");
	char *conference_uuid = switch_event_get_header(event, "Conference-Unique-ID");

	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "event_handler_IM_event conference info conference_Creator:%s,IsPdt:%s,conference_uuid:%s\n", switch_str_nil(conference_Creator), switch_str_nil(conference_CreatorIsPdt),switch_str_nil(conference_uuid));

	if (obj)
	{
		if (!zstr(cCallerSSI))
		{
			cJSON_AddStringToObject(obj, "CallerSSI", cCallerSSI);
		}
		
		cJSON_AddStringToObject(obj, "conference_Creator", conference_Creator);
		cJSON_AddStringToObject(obj, "conference_CreatorIsPdt", conference_CreatorIsPdt);
		cJSON_AddStringToObject(obj, "conference_name", cCalledSSI);
		cJSON_AddStringToObject(obj, "conference_uuid", conference_uuid);
		cJSON_AddStringToObject(obj, "Multicast", cMulticast);
	}

		



	status = IM_pushEvent_Base(cGroupType,MsgToCode, cEventName, obj);
	if (obj)
	{
		cJSON_Delete(obj);
	}
	return status;
}
// switch_status_t event_handler_sys_event(switch_event_t *event) {
// 	switch_status_t status = SWITCH_STATUS_FALSE;
// 	//	uint32_t CallerSSI = pdt_globals.CallerSSI;
// 	char * cEventName = switch_event_get_header(event, "EventName");
// 	cJSON* obj = cJSON_CreateObject();
// 	if (!strcasecmp(cEventName, "IM_Server_On")) {
// 
// 		pdt_globals.bImConnected = SWITCH_TRUE;
// 	}
// 	else if (!strcasecmp(cEventName, "IM_Server_Off")) {
// 		pdt_globals.bImConnected = SWITCH_FALSE;
// 	}
// 	if (obj)
// 	{
// 		cJSON_Delete(obj);
// 	}
// 	return status;
// }
pdt_event_list_t *Pdt_add_EventList(uint32_t nRestryCont, switch_event_t *event)
{
	pdt_event_list_t *rel = NULL;
	switch_event_t *event_new = NULL;
	if (event == NULL || !(rel = switch_core_alloc(pdt_globals.pool, sizeof(*rel))))
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "event NULL\n");
		return NULL;
	}
	if (switch_event_dup(&event_new, event) == SWITCH_STATUS_SUCCESS) {
		switch_mutex_lock(pdt_globals.mutex_event_list);
		rel->id = ++g_nEventListLastId;
		rel->event = event_new;
		rel->nRetryCount = nRestryCont;
		rel->next = pdt_globals.event_list;
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_EventList id:%d,nRestryCont:%d\n", rel->id,
			nRestryCont);
		pdt_globals.event_list = rel;
		if (pdt_globals.cond_thread_event_list != NULL)
		{
			switch_thread_cond_signal(pdt_globals.cond_thread_event_list);
		}
		switch_mutex_unlock(pdt_globals.mutex_event_list);
	}
	return rel;
}
switch_status_t Pdt_add_EventList_Tail(pdt_event_list_t *relNew)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	pdt_event_list_t *rel = NULL;
	pdt_event_list_t *last = NULL;
	if (relNew == NULL)
	{
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_EventList_Tail NULL\n");
		return status;
	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_add_EventList_Tail eventID:%d,nRetryCount:%d\n", relNew->id, relNew->nRetryCount);
	switch_mutex_lock(pdt_globals.mutex_event_list);
	relNew->next = NULL;
	if (pdt_globals.event_list == NULL)
	{
		pdt_globals.event_list = relNew;
		status = SWITCH_STATUS_SUCCESS;
	}
	else {
		for (rel = pdt_globals.event_list; rel; rel = rel->next) {
			if (rel->next == NULL)
			{
				last = rel;
				break;
			}
		}
		if (last)
		{
			last->next = relNew;
			status = SWITCH_STATUS_SUCCESS;
		}
	}


	switch_mutex_unlock(pdt_globals.mutex_event_list);

	return status;
}
switch_status_t Pdt_del_EventList_Top(/*pdt_event_list_t **deleted*/)
{
	switch_status_t status = SWITCH_STATUS_FALSE;
	pdt_event_list_t *rel = NULL;

	switch_mutex_lock(pdt_globals.mutex_event_list);
	rel = pdt_globals.event_list;
	if (rel != NULL)
	{
// 		if (deleted != NULL)
// 		{
// 			*deleted = rel;
// 		}
		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_del_EventList_Top eventID:%d\n", rel->id);
		pdt_globals.event_list = rel->next;
		status = SWITCH_STATUS_SUCCESS;
	}
// 	for (rel = pdt_globals.event_list; rel; rel = rel->next) {
// 		
// 		status = SWITCH_STATUS_SUCCESS;
// 		switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt_del_getcallinfo_mid Delete One\n");
// 
// 		if (last) {
// 			last->next = rel->next;
// 		}
// 		else {
// 			pdt_globals.event_list = rel->next;
// 		}
// 
// 		continue;
// 		
// 
// 		last = rel;
// 	}
	switch_mutex_unlock(pdt_globals.mutex_event_list);

	return status;
}
pdt_event_list_t *Pdt_get_EventList_Top()
{
//	pdt_event_list_t *rel = NULL;
	uint32_t id = -1;
	if (pdt_globals.event_list != NULL)
	{
		id = pdt_globals.event_list->id;
	}
	switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_get_EventList_Top EventID:%d\n", id);
	return pdt_globals.event_list;
//	switch_mutex_lock(pdt_globals.mutex_event_list);
// 	for (rel = pdt_globals.event_list; rel; rel = rel->next) {
// 		if (rel->CallerSSI == CallerSSI) {
// 			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_get_pdtcallinfo_caller GET CalledSSI:%d,CallerSSI:%d,Mid:%d,CallType:%d,CallFromType:%d\n",
// 				rel->CalledSSI, rel->CallerSSI, rel->Mid, rel->CallType, rel->CallFromType);
// 			break;
// 		}
// 	}
//	switch_mutex_unlock(pdt_globals.mutex_event_list);
//	return rel;
}

switch_status_t Pdt_event_handler_exeute(switch_event_t *event)
{
	switch_status_t status = SWITCH_STATUS_SUCCESS;
	if (event->event_id == SWITCH_EVENT_CUSTOM) {
		const char *subclass = switch_event_get_header(event, "Event-Subclass");
		if (!strcasecmp(subclass, CONF_EVENT_MAINT)) {
			//处理conference模块的消息
			char *action = switch_event_get_header(event, "Action");
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_event_handler_exeute action:%s!\n", action);
			if (!strcasecmp(action, "stop-recording")) {
				char *file = switch_event_get_header(event, "Path");
				char *conference_name = switch_event_get_header(event, "conference-name");
				char *CallerSSI = switch_event_get_header(event, "CallerSSI");
				char *CalledSSI = switch_event_get_header(event, "CalledSSI");
				char *cSeconds = switch_event_get_header(event, "Milliseconds-Elapsed");
				char *cMulticast = switch_event_get_header(event, "Multicast");
				char *conference_uuid = switch_event_get_header(event, "Conference-Unique-ID");
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_even stop-recording file:%s,conference_name:%s,CallerSSI:%s,CalledSSI:%s,cMulticast:%s,conference_uuid:%s\n",
					switch_str_nil(file), switch_str_nil(conference_name), switch_str_nil(CallerSSI), switch_str_nil(CalledSSI), switch_str_nil(cMulticast), switch_str_nil(conference_uuid));

				char* MsgToCode = "";
				char* UserCode = "";
				if (zstr(CalledSSI))
				{
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushRecording CalledSSI is empty Use conference_name:%s\n", conference_name);
					CalledSSI = conference_name;
				}

				if (!strcasecmp(cMulticast, "1")) {
					//组呼时，需要用PDT组呼号向IM请求APP组号
					MsgToCode = IM_getPdtAndAppGroup(CalledSSI, SWITCH_FALSE);
				}
				else {
					//单呼时
					MsgToCode = IM_getAppUserID(CalledSSI, GET_All);
				}
				if (CallerSSI)
				{
					UserCode = IM_getAppUserID(CallerSSI, GET_All);
				}
				status = IM_pushRecordingWithFile(file, MsgToCode, UserCode, cMulticast, cSeconds, conference_uuid);
// 				char* fCode = IM_Postfile(file);
// 				if (fCode)
// 				{
// 					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushRecording CallerSSI:%s,CalledSSI:%scMulticast:%s fCode:%s!\n", CallerSSI, CalledSSI,cMulticast, fCode);
// 					const char* cGroupType = "";
// 					const char* MsgToCode = "";
// 					const char* UserCode = "";
// 
// 					if (!strcasecmp(cMulticast, "1")) {
// 						cGroupType = "Group";
// 						MsgToCode = IM_getPdtAndAppGroup(CalledSSI, SWITCH_FALSE);
// 					} else {
// 						cGroupType = "Person";
// 						//单呼时
// 						MsgToCode = IM_getAppUserID(CalledSSI,GET_All);
// 					}
// 			 		if (CallerSSI)
// 					{
// 			 			UserCode = IM_getAppUserID(CallerSSI, GET_All);
// 					}
// 					status = IM_pushRecording(cGroupType, UserCode, MsgToCode, fCode, switch_safe_atoi(cSeconds, 0));
// 					//switch_status_t status = IM_pushMsg("Group", conference_name,NULL, fCode);
// 
// 					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "IM_pushRecording status:%d!\n", status);
// 				}
// 				else {
// 					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "event stop-recording IM_Postfile Fail!\n");
// 					status = SWITCH_STATUS_FALSE;
// 				}
			}
			else if (!strcasecmp(action, "conference-create")) {
// 				char *conference_name = switch_event_get_header(event, "conference-name");
// 				char *conference_Creator = switch_event_get_header(event, "conference-Creator");
// 				char *conference_CreatorIsPdt = switch_event_get_header(event, "conference-CreatorIsPdt");
// 				char *cMulticast = switch_event_get_header(event, "Multicast");
// 				if (conference_name != NULL)
// 				{
// 
// 					cJSON* obj = cJSON_CreateObject();
// 					if (obj)
// 					{
// 						cJSON_AddItemToObject(obj, "conference_Creator", cJSON_CreateString(conference_Creator));
// 						cJSON_AddItemToObject(obj, "conference_CreatorIsPdt", cJSON_CreateString(conference_CreatorIsPdt));
// 						cJSON_AddItemToObject(obj, "conference_name", cJSON_CreateString(conference_name));
// 						cJSON_AddItemToObject(obj, "Multicast", cJSON_CreateString(cMulticast));
// 					}
// 					if (ToCallType(cMulticast) == CALL_Type_Multicast) {
// 						char* appGroupID = IM_getPdtAndAppGroup(conference_name, SWITCH_FALSE);
// 						IM_pushEvent_Group(appGroupID, "conference_create", obj);
// 					} else {
// 						//单呼时
// 						char* AppPersonID = IM_getAppUserID(conference_name, GET_All);
// 						IM_pushEvent_Person(AppPersonID, "conference_create", obj);
// 					}
// 					if (obj)
// 					{
// 						cJSON_Delete(obj);
// 					}
// 				}

			}
			else if (!strcasecmp(action, "conference-add-pdt")) {
				//由app端发起的会议请求。使用会议名称作为pdt的组ID。
				char *conference_name = switch_event_get_header(event, "conference-name");
				char *cMulticast = switch_event_get_header(event, "Multicast");
				char *cCallerSSI = switch_event_get_header(event, "CallerSSI");
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT event conference_name:%s,Multicast:%s!\n", conference_name, cMulticast);
				//uint32_t calledSSI = IM_getPdtGroupID(conference_name);
				uint32_t CalledSSI = switch_safe_atoi(conference_name, 0);
				uint32_t CallerSSI = switch_safe_atoi(cCallerSSI, 0);
				if (CallerSSI == 0)
				{
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "no CallerSSI use pdt_globals.CallerSSI:%d!\n", pdt_globals.CallerSSI);
					CallerSSI = pdt_globals.CallerSSI;
				}
				if (CalledSSI != 0)
				{
					uint32_t CallType = ToCallType(cMulticast);
					switch_status_t statusMakecall = PDT_API_Makecall(pdt_globals.AgentNo, CalledSSI, CallerSSI, 0, CallType, 300, 0);
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "event conference-add-pdt makecall status:%d!\n", statusMakecall);
#ifdef USE_pdtcalllist
					if (statusMakecall == SWITCH_STATUS_SUCCESS)
					{
						Pdt_add_pdtcallinfo(CALL_From_App, CalledSSI, CallerSSI, CallType, 0);
					}
#endif // USE_pdtcalllist
				}
				else {
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "event conference-add-pdt get Pdt Group ID Fail!\n");
				}
			}
			else if (!strcasecmp(action, "ptt-on")) {
				//APP端请求发话。通知PDT开始接收语音。
				event_handler_ptt(event, PTT_STATE_ON);
			}
			else if (!strcasecmp(action, "ptt-off")) {
				event_handler_ptt(event, PTT_STATE_OFF);
			}
			else if (!strcasecmp(action, "im-event")) {
				status = event_handler_IM_event(event);
			}
// 			else if (!strcasecmp(action, "sys-event")) {
// 				status = event_handler_sys_event(event);
// 			}
			else if (!strcasecmp(action, "pdt_send_sms")) {
				char *CallerSSI = switch_event_get_header(event, "CallerSSI");
				char *CalledSSI = switch_event_get_header(event, "CalledSSI");
				char *Multicast = switch_event_get_header(event, "Multicast");
				char *body = switch_event_get_body(event);
				switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "PDT Event pdt_send_sms CallerSSI:%s!,CalledSSI:%s,Multicast:%s,body:[%s]\n", CallerSSI, CalledSSI, Multicast, body);
				if (PDT_API_SMS(pdt_globals.AgentNo, switch_safe_atoi(CalledSSI, 0), switch_safe_atoi(CallerSSI, 0), switch_safe_atoi(Multicast, 0), body, (uint16_t)strlen(body) + 1) != SWITCH_STATUS_SUCCESS) {
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "event pdt_send_sms FAIL !\n");
				}
			}
		}
	}
	return status;
}
void Pdt_event_handler(switch_event_t *event)
{
	switch_bool_t bAddList = SWITCH_FALSE;
	if (event->event_id == SWITCH_EVENT_CUSTOM) {
		const char *subclass = switch_event_get_header(event, "Event-Subclass");
		if (!strcasecmp(subclass, CONF_EVENT_MAINT)) {
			//处理conference模块的消息
			char *action = switch_event_get_header(event, "Action");
			if (!strcasecmp(action, "stop-recording")) {
				bAddList = SWITCH_TRUE;
			}
			else if (!strcasecmp(action, "im-event")) {
				bAddList = SWITCH_TRUE;
			}
			switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "Pdt_event_handler action:%s,bAddList:%d\n", action,bAddList);
			if (bAddList)
			{
				Pdt_add_EventList(pdt_globals.threadEventListRetryCount, event);
			}
			else
			{
				Pdt_event_handler_exeute(event);
			}
		}
	}

}
void *SWITCH_THREAD_FUNC pdt_thread_event_list(switch_thread_t *thread, void *obj)
{
	while (pdt_globals.running) {
		switch_status_t status = switch_thread_cond_timedwait(pdt_globals.cond_thread_event_list, pdt_globals.mutex_event_list, pdt_globals.threadEventListInterval);
		status = SWITCH_STATUS_SUCCESS;//不管是否有信号。都执行循环体
		if (status == SWITCH_STATUS_SUCCESS)
		{
			while (pdt_globals.event_list)
			{
				pdt_event_list_t* eventList = Pdt_get_EventList_Top();
				if (eventList != NULL) {
					Pdt_del_EventList_Top();
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt_thread_event_list Pop EventID:%d,nRetryCount:%d\n",eventList->id, eventList->nRetryCount);
					if (Pdt_event_handler_exeute(eventList->event) == SWITCH_STATUS_SUCCESS) {
						//执行成功从队列中删除
						switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt_thread_event_list handler_exeute SUCC:%d\n", eventList->id);

					} else {
						//执行失败则加入到队尾。等候重试
						if (eventList->nRetryCount > 0)
						{
							switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "pdt_thread_event_list handler_exeute FAIL EventID:%d,nRetryCount:%d\n", eventList->id, eventList->nRetryCount);
							eventList->nRetryCount--;
							Pdt_add_EventList_Tail(eventList);
						} else {
							switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_ERROR, "pdt_thread_event_list handler_exeute FAIL EventID:%d nRetryCount=0 Delete It\n", eventList->id);
						}
					}
					
				}
				if (eventList == pdt_globals.event_list)
				{
					switch_log_printf(SWITCH_CHANNEL_LOG, SWITCH_LOG_INFO, "pdt_thread_event_list Get Tail wait Next Try\n");
					break;
				}
			}
		}
	}

	return NULL;
}
void pdt_launch_thread_Eventlist()
{
	switch_thread_t *thread;
	switch_threadattr_t *thd_attr = NULL;

	switch_threadattr_create(&thd_attr, pdt_globals.pool);
	switch_threadattr_detach_set(thd_attr, 1);
	switch_threadattr_priority_set(thd_attr, SWITCH_PRI_REALTIME);
	switch_threadattr_stacksize_set(thd_attr, SWITCH_THREAD_STACKSIZE);
	switch_thread_create(&thread, thd_attr, pdt_thread_event_list, NULL, pdt_globals.pool);
	pdt_globals.threadConnect = thread;
}




void Pdt_event_record_stop(switch_event_t *event)
{//APP呼叫APP时的录像处理

	//	switch_bool_t bAddList = SWITCH_FALSE;
	//	if (event->event_id == SWITCH_EVENT_CUSTOM) {
	//		const char *subclass = switch_event_get_header(event, "Event-Subclass");
	//		if (!strcasecmp(subclass, CONF_EVENT_MAINT)) {
	//处理conference模块的消息
// 	char *action = switch_event_get_header(event, "Action");
// 	char *BridgedTime = switch_event_get_header(event, "Caller-Channel-Bridged-Time");
	char *AnsweredTime = switch_event_get_header(event, "Caller-Channel-Answered-Time");
	char *HangupTime = switch_event_get_header(event, "Caller-Channel-Hangup-Time");
	char *Caller = switch_event_get_header(event, "Caller-Caller-ID-Number");
	char *Called = switch_event_get_header(event, "Caller-Callee-ID-Number");
// 	char *CallerSSI = switch_event_get_header(event, "variable_CallerSSI");
// 	char *CalledSSI = switch_event_get_header(event, "variable_CalledSSI");
	char *RecordFilePath = switch_event_get_header(event, "Record-File-Path");
	char *record_filename = switch_event_get_header(event, "variable_record_filename");
	char cSeconds[100] = {0};
	if (!zstr(HangupTime) && !zstr(AnsweredTime))
	{
		int nEndPos = strlen(HangupTime) - 6;
		HangupTime[nEndPos] = '\0';
		uint32_t nHangupTime = switch_safe_atoi(HangupTime, 0);

		int nEndPos2 = strlen(AnsweredTime) - 6;
		AnsweredTime[nEndPos2] = '\0';
		uint32_t nAnsweredTime = switch_safe_atoi(AnsweredTime, 0);
		switch_snprintf(cSeconds, sizeof(cSeconds), "%d", (nHangupTime - nAnsweredTime)*1000);
	}
	if (!zstr(Caller) && !zstr(Called) && !zstr(RecordFilePath))
	{
		IM_pushRecordingWithFile(RecordFilePath, Called, Caller, "0", cSeconds,NULL);
	}
	//		}
	//	}

}