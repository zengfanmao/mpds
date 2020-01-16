#ifndef IM_API_HEAD_H
#define IM_API_HEAD_H

#include <switch.h>
switch_status_t IM_Login();
switch_bool_t Fs_cmd_pdt(const char* cmd, const char* arg, char** pcGetResult);

char* IM_Postfile(char* file);
uint32_t IM_getPdtGroupID(char* AppGroupId);
char* IM_getAppGroupID(uint32_t pdtSSI);
char* IM_getPdtAndAppGroup(char* code, switch_bool_t bGetPdt);
////返回IM中APP账户的用户ID
char* IM_getAppUserID(char* code, ENUM_GET_TYPE GetType);
char* IM_getAppUserID_SSI(uint32_t pdtSSI, ENUM_GET_TYPE GetType);
// char* IM_getAppUserID(char* code);
// char* IM_getAppUserID_SSI(uint32_t pdtSSI);
switch_status_t IM_pushMsg(const char* MsgFromType, const char* MsgToCode, const char* MsgContent, const char* MsgFile, const char* SenderUserCode);
switch_status_t IM_pushRecording(const char* MsgFromType, const char* UserCode, const char* MsgToCode, const char* MsgFile, int Second, const char* conference_uuid);
switch_status_t IM_pushRecordingWithFile(char *file, char* MsgToCode, char* UserCode, const char* cMulticast, const char *cSeconds, const char* conference_uuid);
switch_status_t IM_pushCommand(const char* MsgFromType, const char* MsgToCode, const char* MsgContent);
// switch_status_t IM_pushEvent_Group(char *pdtID, const char* cEventName, cJSON* obj);
// switch_status_t IM_pushEvent_Person(char *appUserCode, const char* cEventName, cJSON* obj);
switch_status_t IM_pushEvent_Base(const char* MsgFromType, char *appGroupID, const char* cEventName, cJSON* obj);
switch_bool_t IM_IsPdtSSI(uint32_t pdtSSI);
switch_bool_t IM_IsAppSSI(uint32_t pdtSSI);
switch_status_t IM_getIMStatus();
#endif //IM_API_HEAD_H