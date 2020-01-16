package com.aimissu.ptt.config;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.aimissu.basemvp.net.rx.LogUtil;
import com.aimissu.basemvp.net.rx.RxUtils;
import com.aimissu.basemvp.utils.ToastUtils;
import com.aimissu.ptt.app.MyApp;
import com.aimissu.ptt.db.LocalCache;
import com.aimissu.ptt.db.MySQLiteOpenHelper;
import com.aimissu.ptt.entity.ReadMsgEvent;
import com.aimissu.ptt.entity.im.IMType;
import com.aimissu.ptt.entity.local.DaoMaster;
import com.aimissu.ptt.entity.local.DaoSession;
import com.aimissu.ptt.entity.local.LocalChatMsgList;
import com.aimissu.ptt.entity.local.LocalChatMsgListDao;
import com.aimissu.ptt.entity.local.LocalMsgCount;
import com.aimissu.ptt.entity.local.LocalMsgCountDao;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.local.LocalPersonUserEntity;
import com.aimissu.ptt.entity.local.LocalPersonUserEntityDao;
import com.aimissu.ptt.entity.local.LocalServer;
import com.aimissu.ptt.entity.local.LocalServerDao;
import com.aimissu.ptt.entity.local.LocalUserEntity;
import com.aimissu.ptt.entity.local.LocalUserEntityDao;
import com.aimissu.ptt.entity.local.LocalUserGroup;
import com.aimissu.ptt.entity.local.LocalUserGroupDao;
import com.aimissu.ptt.entity.local.LogConfig;
import com.aimissu.ptt.entity.local.LogConfigDao;
import com.aimissu.ptt.entity.ui.ChatMsgList;
import com.aimissu.ptt.entity.ui.PersonUserEntity;
import com.aimissu.ptt.entity.ui.UserEntity;
import com.aimissu.ptt.entity.ui.UserGroup;
import com.aimissu.ptt.utils.CommonUtils;
import com.aimissu.ptt.utils.Configs;
import com.aimissu.ptt.utils.Global;
import com.aimissu.ptt.utils.SharePreferenceUtil;
import com.aimissu.ptt.view.widget.audio.FileUtils;
import com.mabeijianxi.smallvideorecord2.DeviceUtils;
import com.mabeijianxi.smallvideorecord2.JianXiCamera;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.internal.Util;

/**
 */
public class AppManager {
    private static volatile MyApp mApp;
    private static volatile DaoSession daoSession;
    public static final boolean ENCRYPTED = true;
    private static String tag = "AppManager";
    public static String IMserver = "";
    public static String CURRENT_GROUPNUMBER = "CURRENT_GROUPNUMBER";
    public static String callName = "";
    public static boolean personPdtCall;
    //正在讲话的人
    public static String speakingName = "";
    public static String speakingDeviceId = "";

    public static volatile boolean needSedPwd = false;
    public static String conferenceCreator;

    public static int HEADSET_PLUG_STATE = 0;

//    public static String getIMserver() {
//        return IMserver;
//    }
//
//    public static void setIMserver(String IMserver) {
////        "tcp://114.115.165.67:1883";
//        AppManager.IMserver = "tcp://" + IMserver + ":1883";
//    }

    public static DaoSession initDaoSession() {
        if (daoSession == null) {
            synchronized (AppManager.class) {
                if (daoSession == null) {
                    MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getApp().getApplicationContext(), ENCRYPTED ? "xqc-db-encrypted" : "xqc-db", null);
                    Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
                    daoSession = new DaoMaster(db).newSession();
                }
            }
        }
        return daoSession;
    }


    public static DaoSession getDaoSession() {
        return initDaoSession();
    }

    public static void setApp(MyApp app) {
        if (mApp == null) {
            synchronized (AppManager.class) {
                if (mApp == null) {
                    mApp = app;
                }
            }
        }
    }

    public static MyApp getApp() {
        return mApp;
    }

    /**
     * 清楚用户数据
     */
    public static void clearUser() {
        getDaoSession().getLocalUserEntityDao().detachAll();
        getDaoSession().getLocalUserEntityDao().deleteAll();
    }

    /**
     * 设置本地用户数据
     *
     * @param localUserEntity
     */
    public static void setLocalUserData(LocalUserEntity localUserEntity) {
        getDaoSession().getLocalUserEntityDao().insertOrReplace(localUserEntity);
    }

    /**
     * 更新本地用户数据
     *
     * @param localUserEntity
     */
    public static void updateLocalUserData(LocalUserEntity localUserEntity) {
        getDaoSession().getLocalUserEntityDao().update(localUserEntity);
    }

    /**
     * 获取用户数据
     *
     * @return
     */
    public static UserEntity getUserData() {
        try {
            List<LocalUserEntity> userEntities = getDaoSession().getLocalUserEntityDao().loadAll();
            if (userEntities != null && userEntities.size() > 0) {
                return userEntities.get(0).toModel();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean isLogin() {
        UserEntity userEntity = getUserData();
        return userEntity == null ? false : true;
    }

    public static String getLoginName() {
        UserEntity userEntity = getUserData();
        LogUtil.i(tag, "userEntity:" + userEntity.toString());
        return userEntity == null ? null : (TextUtils.isEmpty(userEntity.getLoginName()) ? "" : userEntity.getLoginName().trim());
    }

    public static String getdCode() {
        UserEntity userEntity = getUserData();
        LogUtil.i(tag, "userEntity:" + userEntity.toString());
        return userEntity == null ? null : (TextUtils.isEmpty(userEntity.getLoginName()) ? "" : userEntity.getdCode().trim());
    }

    public static String getUserCode() {
        UserEntity userEntity = getUserData();
        LogUtil.i(tag, "userEntity:" + userEntity.toString());
        return userEntity == null ? null : (TextUtils.isEmpty(userEntity.getLoginName()) ? "" : userEntity.getUserCode().trim());
    }

    public static String getUserDeviceId() {
        UserEntity userEntity = getUserData();
        LogUtil.i(tag, "userEntity:" + userEntity.toString());
        return userEntity == null ? null : (TextUtils.isEmpty(userEntity.getLoginName()) ? "" : userEntity.getDeviceId().trim());
//        return "";
    }

    public static String getLoginNameInOtherProcess() {
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.aimissu.ptt/user");
            cursor = getApp().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    return CommonUtils.emptyIfNull(cursor.getString(cursor.getColumnIndex(LocalUserEntityDao.Properties.LoginName.columnName))).trim();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return "";
    }

    public static String getUserName() {
        UserEntity userEntity = getUserData();
        return userEntity == null ? null : (TextUtils.isEmpty(userEntity.getUserName()) ? "" : userEntity.getUserName().trim());
    }

    public static String getApiToken() {
        UserEntity userEntity = getUserData();
        return userEntity == null ? null : (TextUtils.isEmpty(userEntity.getApiToken()) ? "" : userEntity.getApiToken().trim());
    }

    private static volatile SharePreferenceUtil sharePreferenceUtil;

    public static SharePreferenceUtil getSharePreferenceUtil() {
        if (sharePreferenceUtil == null) {
            synchronized (AppManager.class) {
                if (sharePreferenceUtil == null) {
                    sharePreferenceUtil = new SharePreferenceUtil(getApp().getApplicationContext(), MyApp.class.getName());
                }
            }
        }
        return sharePreferenceUtil;
    }

    /**
     * 以讨论组号保存pdt组号
     *
     * @param disscusionCode
     * @param value
     */
    public static void setPdtNumberWithDisscusionCode(String disscusionCode, String value) {
        getSharePreferenceUtil().writeStringValue(disscusionCode, value);
    }

    /**
     * 根据讨论组号获取pdt组号
     *
     * @param disscusionCode
     * @return
     */
    public static String getPdtNumberWithDisscusionCode(String disscusionCode) {
        return getSharePreferenceUtil().getStringValue(disscusionCode);
    }

    /**
     * 以组号保存通话状态
     *
     * @param groupNumber
     * @param value
     */
    public static void setCallSatusWithGroupNumber(String groupNumber, String value) {
        getSharePreferenceUtil().writeStringValue(groupNumber, value);
    }

    /**
     * 根据组号获取通话状态
     *
     * @param groupNumber
     * @return
     */
    public static String getCallSatusWithGroupNumber(String groupNumber) {
        return getSharePreferenceUtil().getStringValue(groupNumber);
    }

    /**
     * 保存当前的守候组
     *
     * @param value
     * @param value
     */
    public static void setCurrentGroupNumber(String value) {
        getSharePreferenceUtil().writeStringValue(CURRENT_GROUPNUMBER, value);
    }

    /**
     * 获取当前的守候组
     *
     * @param
     * @return
     */
    public static String getCurrentGroupNumber() {
        return getSharePreferenceUtil().getStringValue(CURRENT_GROUPNUMBER);
    }

    public static void msgCountGroup() {
        int value = getSharePreferenceUtil().getIntValue(IMType.Params.MSG_GROUP_SUM, 0) + 1;
        getSharePreferenceUtil().writeIntValue(IMType.Params.MSG_GROUP_SUM, value);
    }

    public static void msgCountPerson() {
        int value = getSharePreferenceUtil().getIntValue(IMType.Params.MSG_PERSON_SUM, 0) + 1;
        getSharePreferenceUtil().writeIntValue(IMType.Params.MSG_PERSON_SUM, value);
    }

    public static volatile ActivityManager manager;

    public static boolean isServiceRunning(Class<?> mClass) {
        if (manager == null) {
            manager = (ActivityManager) getApp().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (mClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 初始化录视频
     */
    public static void initSmallVideo() {
        // 设置拍摄视频缓存路径
        File dcim = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        if (DeviceUtils.isZte()) {
            if (dcim.exists()) {
                JianXiCamera.setVideoCachePath(dcim + "/ptt/");
            } else {
                JianXiCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/",
                        "/sdcard-ext/")
                        + "/ptt/");
            }
        } else {
            JianXiCamera.setVideoCachePath(dcim + "/ptt/");
        }
        // 初始化拍摄
        JianXiCamera.initialize(false, null);
    }

    public static void setHadRedMsgCount(String msgId, String type) {
        setHadRedMsgCount(msgId, type, true);
    }

    /**
     * 设置已经阅读了的消息数
     *
     * @param msgId
     */
    public static void setHadRedMsgCount(String msgId, String type, boolean refresh) {
        try {
            String userCode = AppManager.getLoginName();
            if (TextUtils.isEmpty(userCode))
                return;
            LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
            LocalMsgCount localMsgCount = getFirstMsgCount(localMsgCountDao.queryBuilder().where(LocalMsgCountDao.Properties.UserCode.eq(userCode),
                    LocalMsgCountDao.Properties.MsgId.eq(msgId),
                    LocalMsgCountDao.Properties.Type.eq(type)).list());
            if (localMsgCount != null) {
                int allCount = CommonUtils.toInt(localMsgCount.getMsgCount());
                localMsgCount.setReadCount(String.valueOf(allCount));
                localMsgCount.setUserCode(userCode);
                localMsgCountDao.insertOrReplaceInTx(localMsgCount);
                if (refresh) {
                    EventBus.getDefault().post(new ReadMsgEvent(msgId, type));
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void setHadRedAllMsgCount(String type, boolean refresh) {
        try {
            String userCode = AppManager.getLoginName();
            if (TextUtils.isEmpty(userCode))
                return;
            LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
            List<LocalMsgCount> localMsgCounts = localMsgCountDao.queryBuilder().where(LocalMsgCountDao.Properties.UserCode.eq(userCode),
                    LocalMsgCountDao.Properties.Type.eq(type)).list();
            if (localMsgCounts != null && localMsgCounts.size() > 0) {
                for (LocalMsgCount localMsgCount : localMsgCounts) {
                    if (localMsgCount != null) {
                        int allCount = CommonUtils.toInt(localMsgCount.getMsgCount());
                        localMsgCount.setReadCount(String.valueOf(allCount));
                    }
                }
            }
            localMsgCountDao.insertOrReplaceInTx(localMsgCounts);
            if (refresh) {
                EventBus.getDefault().post(new ReadMsgEvent("READ_ALL", type));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void setMsgCountIncrease(String msgId, String type) {
        setMsgCountIncrease(msgId, type, true);
    }

    public static void setMsgCountIncrease(String msgId, String type, boolean refresh) {
        try {
            String userCode = AppManager.getLoginName();
            if (TextUtils.isEmpty(userCode))
                return;
            LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
            LocalMsgCount localMsgCount = getFirstMsgCount(localMsgCountDao.queryBuilder().where(LocalMsgCountDao.Properties.UserCode.eq(userCode),
                    LocalMsgCountDao.Properties.MsgId.eq(msgId.trim()),
                    LocalMsgCountDao.Properties.Type.eq(type)).list());
            if (localMsgCount != null) {
                localMsgCount.setMsgCount(String.valueOf(CommonUtils.toInt(localMsgCount.msgCount) + 1));
                localMsgCount.setUserCode(userCode);
                localMsgCountDao.insertOrReplaceInTx(localMsgCount);
                if (refresh) {
                    EventBus.getDefault().post(new ReadMsgEvent(msgId, type));
                }
                Log.e("MqttService", "showNotify setMsgCountIncrease+1 ：" + msgId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setAllMsgCountGroup(List<UserGroup> userGroups, Runnable runnable) {
        RxUtils.asyncRun(new Runnable() {
            @Override
            public void run() {
                try {
                    if (userGroups == null)
                        return;
                    String userCode = AppManager.getLoginName();
                    if (TextUtils.isEmpty(userCode))
                        return;
                    synchronized (this) {
                        LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
                        List<LocalMsgCount> localMsgCounts = new ArrayList<>();
                        for (UserGroup userGroup : userGroups) {
                            if (userGroup != null && userGroup.getGroupid() != null) {

                                LocalMsgCount localMsgCount = getFirstMsgCount(localMsgCountDao.queryBuilder().where(LocalMsgCountDao.Properties.UserCode.eq(userCode),
                                        LocalMsgCountDao.Properties.MsgId.eq(userGroup.getGroupid()),
                                        LocalMsgCountDao.Properties.Type.eq(IMType.MsgFromType.Group.toString())).list());
                                if (localMsgCount == null) {
                                    localMsgCount = new LocalMsgCount();
                                    localMsgCount.setReadCount("0");
                                    localMsgCount.setType(IMType.MsgFromType.Group.toString());
                                    localMsgCount.setMsgId(userGroup.getGroupid());
                                }
                                localMsgCount.setUserCode(userCode);
                                localMsgCount.setIsDefault(userGroup.getIsdefault());

                                //uIsUnitlt字段？ by cuizh,0407
                                if (CommonUtils.toInt(userGroup.getuIsUnilt()) > 0)
                                    localMsgCount.setMsgCount(String.valueOf(CommonUtils.toInt(userGroup.getuIsUnilt())));

//                                LogUtil.i(tag,"LocalMsgCounts test: :   "+localMsgCount.getMsgId()+"　　　　"+localMsgCount.getIsDefault());

                                localMsgCounts.add(localMsgCount);
                            }
                        }
                        localMsgCountDao.insertOrReplaceInTx(localMsgCounts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (runnable != null)
                    runnable.run();
            }
        });
    }

    private static LocalMsgCount getFirstMsgCount(List<LocalMsgCount> localMsgCounts) {
        if (localMsgCounts != null && localMsgCounts.size() > 0) {
            return localMsgCounts.get(0);
        } else {
            return null;
        }
    }

    public static void setAllMsgCountPerson(List<PersonUserEntity> userEntities, Runnable runnable) {
        RxUtils.asyncRun(new Runnable() {
            @Override
            public void run() {
                try {
                    if (userEntities == null)
                        return;
                    String userCode = AppManager.getLoginName();
                    if (TextUtils.isEmpty(userCode))
                        return;
                    synchronized (this) {
                        LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
                        List<LocalMsgCount> localMsgCounts = new ArrayList<>();

                        for (PersonUserEntity userEntity : userEntities) {
                            if (userEntity != null && userEntity.getuCode() != null) {
                                LocalMsgCount localMsgCount = getFirstMsgCount(localMsgCountDao.queryBuilder()
                                        .where(LocalMsgCountDao.Properties.UserCode.eq(userCode),
                                                LocalMsgCountDao.Properties.MsgId.eq(userEntity.getuCode().trim()),
                                                LocalMsgCountDao.Properties.Type.eq(IMType.MsgFromType.Person.toString())).list());
                                if (localMsgCount == null) {
                                    localMsgCount = new LocalMsgCount();
                                    localMsgCount.setReadCount("0");
                                    localMsgCount.setType(IMType.MsgFromType.Person.toString());
                                    localMsgCount.setMsgId(userEntity.getuCode().trim());
                                }
                                localMsgCount.setUserCode(userCode);

                                //uIsUnilt字段没有改变？  by cuizh,0407
//                                if (CommonUtils.toInt(userEntity.getuIsUnilt()) > 0)
//                                    localMsgCount.setMsgCount(String.valueOf(CommonUtils.toInt(userEntity.getuIsUnilt())));

                                if (CommonUtils.toInt(userEntity.getuIsUnilt()) > 0
                                        && CommonUtils.toInt(userEntity.getuIsUnilt()) >= CommonUtils.toInt(localMsgCount.getMsgCount())) {

                                    localMsgCount.setMsgCount(String.valueOf(CommonUtils.toInt(userEntity.getuIsUnilt())));

//                                    LogUtil.i(tag,"LocalMsgCounts test: :   "+localMsgCount.getMsgId()+"　　　　"+localMsgCount.getMsgCount());
                                }

                                localMsgCounts.add(localMsgCount);
                            }
                        }
                        localMsgCountDao.insertOrReplaceInTx(localMsgCounts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Runnable() {
            @Override
            public void run() {
                if (runnable != null)
                    runnable.run();
            }
        });
    }

    public static HashMap<String, LocalMsgCount> getMsgCountList(String type) {
        HashMap<String, LocalMsgCount> localMsgCountHashMap = new HashMap<>();
        try {
            String userCode = AppManager.getLoginName();
            if (TextUtils.isEmpty(userCode))
                return localMsgCountHashMap;
            LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
            List<LocalMsgCount> localMsgCounts = localMsgCountDao.queryBuilder().where(LocalMsgCountDao.Properties.UserCode.eq(userCode), LocalMsgCountDao.Properties.Type.eq(type)).list();

            if (localMsgCounts != null) {
                for (LocalMsgCount localMsgCount : localMsgCounts) {
                    if (localMsgCount != null) {
                        if (type.equals(IMType.MsgFromType.Person.toString()) && AppManager.getLoginName().equals(localMsgCount.getMsgId())) {
                            continue;
                        }
                        localMsgCountHashMap.put(localMsgCount.getMsgId(), localMsgCount);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return localMsgCountHashMap;
    }

    /**
     * 获取已阅读消息数
     *
     * @param msgId
     * @return
     */
    public String getHadRedMsgCount(String msgId, String type) {
        if (!TextUtils.isEmpty(msgId)) {
            String userCode = AppManager.getLoginName();
            if (TextUtils.isEmpty(userCode))
                return "0";
            LocalMsgCountDao localMsgCountDao = getDaoSession().getLocalMsgCountDao();
            LocalMsgCount localMsgCount = getFirstMsgCount(localMsgCountDao.queryBuilder().where(
                    LocalMsgCountDao.Properties.UserCode.eq(userCode),
                    LocalMsgCountDao.Properties.MsgId.eq(msgId), LocalMsgCountDao.Properties.Type.eq(type)).list());
            if (localMsgCount != null) {
                return CommonUtils.defaultValueIfNull(localMsgCount.getMsgCount(), "0");
            }
        }
        return "0";
    }


    /**
     * 设置本地消息
     *
     * @param chatMsgLists
     */
    public static void setChatMsgList(List<ChatMsgList> chatMsgLists) {
        if (chatMsgLists != null) {

            List<LocalChatMsgList> localChatMsgLists = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            for (ChatMsgList chatMsgList : chatMsgLists) {
                if (chatMsgList != null) {
                    localChatMsgLists.add(chatMsgList.toLocalChatMsgList());
                    if (!TextUtils.isEmpty(chatMsgList.getMsgId())) {
                        keys.add(chatMsgList.getMsgId().trim());
                    }
                }
            }

            LocalChatMsgListDao localChatMsgListDao = getDaoSession().getLocalChatMsgListDao();
            //删除本地旧数据库的，只有当服务器有的时候才不删除
            List<LocalChatMsgList> list = localChatMsgListDao.queryBuilder().where(LocalChatMsgListDao.Properties.MsgId.in(keys)).list();
            if (list != null) {
                localChatMsgListDao.deleteInTx(list);
            }

            //更新数据库的
            localChatMsgListDao.insertOrReplaceInTx(localChatMsgLists);
        }
    }

    public List<ChatMsgList> getChatMsgList(String msgFromType) {
        LocalChatMsgListDao localChatMsgListDao = getDaoSession().getLocalChatMsgListDao();
        List<LocalChatMsgList> localChatMsgLists = localChatMsgListDao.queryBuilder().where(LocalChatMsgListDao.Properties.MsgFromType.eq(msgFromType)).list();
        List<ChatMsgList> chatMsgLists = new ArrayList<>();
        if (localChatMsgLists != null) {
            for (LocalChatMsgList localChatMsgList : localChatMsgLists) {
                chatMsgLists.add(localChatMsgList.toChatMsgList());
            }
        }
        return chatMsgLists;

    }

    public static String getUserHeadPortrait() {
        UserEntity userEntity = getUserData();
        if (userEntity == null) {
            return null;
        }
        String headPortrait = userEntity.getHeadPortrait();
        if (TextUtils.isEmpty(userEntity.getHeadPortrait())) {
            //获取本地的头像
            LocalMsgFile localMsgFile = FileUtils.getLocalMsgFileByMsgId(AppManager.getUserCode());
            headPortrait = localMsgFile.getLocalFileUrl();
            try {
                File file = new File(headPortrait);
                if (!file.exists()) {
                    headPortrait = localMsgFile.getFileDownloadUrll();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return headPortrait;
    }

    public static void initDirectory() {

        if (!new File(Configs.ImgRoot).isDirectory())
            new File(Configs.ImgRoot).mkdirs();

        if (!new File(Configs.MapRoot).isDirectory())
            new File(Configs.MapRoot).mkdirs();

        if (!new File(Configs.AudioRoot).isDirectory())
            new File(Configs.AudioRoot).mkdirs();


    }

    /**
     * 保存个人用户列表
     *
     * @param personUserLists
     */
    public static void setPersonUserList(List<PersonUserEntity> personUserLists) {

        if (personUserLists != null) {

            List<LocalPersonUserEntity> localPersonUserLists = new ArrayList<>();
            List<Long> keys = new ArrayList<>();
            for (PersonUserEntity personUser : personUserLists) {
                if (personUser != null) {
                    localPersonUserLists.add(personUser.toLocalPersonUserEntity());
                    if (!TextUtils.isEmpty(personUser.getID().toString())) {
                        keys.add(personUser.getID());
                    }
                }
            }
            LocalPersonUserEntityDao localPersonUserEntityDao = getDaoSession().getLocalPersonUserEntityDao();
            //删除本地旧数据库的，只有当服务器有的时候才不删除
            List<LocalPersonUserEntity> list = localPersonUserEntityDao.queryBuilder().where(LocalPersonUserEntityDao.Properties.UCode.notIn(keys)).list();
            if (list != null) {
                localPersonUserEntityDao.deleteInTx(list);
            }


            //测试插入数据库的情况
//            for(LocalPersonUserEntity localPersonUserEntity : localPersonUserLists){
//                LocalPersonUserEntity lpue = localPersonUserEntityDao.load(localPersonUserEntity.getID());
//                if (lpue != null) {
//                    localPersonUserEntityDao.update(localPersonUserEntity);
//
//                    LogUtil.i(tag,"AppManage test: localPersonUserEntityDao.loadAll:   "+localPersonUserEntityDao.loadAll());
//                }else {
//                    localPersonUserEntityDao.insert(localPersonUserEntity);
//                    LogUtil.i(tag,"AppManage test: localPersonUserEntityDao.loadAll:   "+localPersonUserEntityDao.loadAll());
//                }
//            }

            //更新数据库的
            localPersonUserEntityDao.insertOrReplaceInTx(localPersonUserLists);
//            LogUtil.i(tag,"AppManage test: getPersonUserEntityList().size():   "+getPersonUserEntityList().size());

//            Global.getMainHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    LogUtil.i(tag,"AppManage test: getPersonUserEntityList().size():   "+getPersonUserEntityList().size());
//                }
//            },1000);

        }
    }



    /**
     * 增加全部个人列表
     *
     * @return
     */
    public static void addPersonUserEntityList(List<PersonUserEntity> personUserEntityList) {
        List<PersonUserEntity> personUserEntities = getPersonUserEntityList();
        personUserEntities.addAll(personUserEntityList);
        setPersonUserList(personUserEntities);
    }


    /**
     * 获取全部个人列表
     *
     * @return
     */
    public static List<PersonUserEntity> getPersonUserEntityList() {

        LocalPersonUserEntityDao localPersonUserEntityDao = getDaoSession().getLocalPersonUserEntityDao();
        List<LocalPersonUserEntity> localPersonUserEntityList = localPersonUserEntityDao.loadAll();
        List<PersonUserEntity> personUserEntityList = new ArrayList<>();
        if (localPersonUserEntityList != null) {
            for (LocalPersonUserEntity localPersonUserEntity : localPersonUserEntityList) {
                personUserEntityList.add(localPersonUserEntity.toModel());
            }
        }
        return personUserEntityList;
    }


    /**
     * 获取某个成员列表
     *
     * @return
     */
    public static PersonUserEntity getPersonUserEntity(String ucode) {
        LocalPersonUserEntityDao localPersonUserEntityDao = getDaoSession().getLocalPersonUserEntityDao();
        List<LocalPersonUserEntity> localPersonUserEntityList = localPersonUserEntityDao.queryBuilder().where(LocalPersonUserEntityDao.Properties.UCode.eq(ucode)).list();
        if (localPersonUserEntityList != null && localPersonUserEntityList.size() > 0) {
            return localPersonUserEntityList.get(0).toModel();
        }
        return null;
    }

    /**
     * 获取某个成员列表
     *
     * @return
     */
    public static PersonUserEntity getPersonUserEntityBydeviceId(String deviceId) {
        LocalPersonUserEntityDao localPersonUserEntityDao = getDaoSession().getLocalPersonUserEntityDao();
        List<LocalPersonUserEntity> localPersonUserEntityList = localPersonUserEntityDao.queryBuilder().where(LocalPersonUserEntityDao.Properties.Deviceid.eq(deviceId)).list();
        if (localPersonUserEntityList != null && localPersonUserEntityList.size() > 0) {
            return localPersonUserEntityList.get(0).toModel();
        }
        return null;
    }


    /**
     * 更新用户组数据
     *
     * @param localPersonUserEntity
     */
    public static void updateLocalPersonUser(LocalPersonUserEntity localPersonUserEntity) {
        getDaoSession().getLocalPersonUserEntityDao().update(localPersonUserEntity);
    }


    /**
     * 设置用户组列表
     *
     * @param userGroupList
     */
    public static void setUserGroupList(List<UserGroup> userGroupList) {
        if (userGroupList != null) {

            List<LocalUserGroup> localUserGroupList = new ArrayList<>();
            List<String> keys = new ArrayList<>();
            for (UserGroup userGroup : userGroupList) {
                if (userGroup != null) {
                    localUserGroupList.add(userGroup.toLocalUserGroup());
                    if (!TextUtils.isEmpty(userGroup.getGroupid().toString())) {
                        keys.add(userGroup.getGroupid());
                    }
                }
            }

            LocalUserGroupDao localUserGroupDao = getDaoSession().getLocalUserGroupDao();
            //删除本地旧数据库的，只有当服务器有的时候才不删除
//            List<LocalUserGroup> list = localUserGroupDao.queryBuilder().where(LocalUserGroupDao.Properties.Groupid.notIn(keys)).list();
//            if (list != null) {
//                localUserGroupDao.deleteInTx(list);
//            }
            //更新数据库的
            LogUtil.i(tag, "数据库插入前localUserGroupDao.loadAll():" + localUserGroupDao.loadAll().toString());
            for (LocalUserGroup localUserGroup : localUserGroupList) {
                LocalUserGroup lg = localUserGroupDao.load(localUserGroup.getGroupid());
                if (lg != null) {
                    localUserGroup.setSendUserName(lg.getSendUserName());
                    localUserGroup.setSendUserCode(lg.getSendUserCode());
                    localUserGroup.setMsgType(lg.getMsgType());
                    localUserGroup.setMsgContent(lg.getMsgContent());
                    localUserGroup.setCalling(CommonUtils.emptyIfNull(lg.getCalling()));
                    localUserGroupDao.update(localUserGroup);
                    LogUtil.i(tag, "更新数据库····");
                } else {
                    localUserGroupDao.insert(localUserGroup);
                    LogUtil.i(tag, "插入数据库····");
                }
            }

            LogUtil.i(tag, "数据库插入后localUserGroupDao.loadAll():" + localUserGroupDao.loadAll().toString());
            ;
//            localUserGroupDao.insertOrReplaceInTx(localUserGroupList);

        }
    }

    /**
     * 获取全部用户组列表
     *
     * @return
     */
    public static List<UserGroup> getUserGroupList() {
        LocalUserGroupDao localUserGroupDao = getDaoSession().getLocalUserGroupDao();
        List<LocalUserGroup> localUserGroupList = localUserGroupDao.loadAll();
        List<UserGroup> userGroupList = new ArrayList<>();
        if (localUserGroupList != null) {
            for (LocalUserGroup localUserGroup : localUserGroupList) {
                userGroupList.add(localUserGroup.toUserGroup());
            }
        }
        return userGroupList;
    }

    /**
     * 更新用户组数据
     *
     * @param localUserGroup
     */
    public static void updateLocalUserGroup(LocalUserGroup localUserGroup) {
        getDaoSession().getLocalUserGroupDao().update(localUserGroup);
        LogUtil.i(tag, "更新之后的数据localUserGroupDao.loadAll():" + getDaoSession().getLocalUserGroupDao().loadAll().toString());
    }

    /**
     * 获取某个用户组列表
     *
     * @return
     */
    public static UserGroup getUserGroup(String groupId) {
        try {
            LocalUserGroupDao localUserGroupDao = getDaoSession().getLocalUserGroupDao();
            List<LocalUserGroup> localUserGroupList = localUserGroupDao.queryBuilder().where(LocalUserGroupDao.Properties.Groupid.eq(groupId)).list();
            if (localUserGroupList != null && localUserGroupList.size() > 0) {
                return localUserGroupList.get(0).toUserGroup();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取缓存中用户信息
     *
     * @param userCode
     * @return
     */
    public static PersonUserEntity getUserEntityCache(String userCode) {
        List<PersonUserEntity> userEntities = AppManager.getPersonUserEntityList();
        if (userEntities != null) {
            for (PersonUserEntity userEntity : userEntities) {
                if (userEntity != null && !TextUtils.isEmpty(userEntity.getuCode()) && userEntity.getuCode().equals(userCode)) {
                    return userEntity;
                }
            }
        }
        return null;
    }

    public static HashMap<String, Integer> getAllUnRead() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        HashMap<String, LocalMsgCount> localMsgCountHashMapGroup = AppManager.getMsgCountList(IMType.MsgFromType.Group.toString());
        HashMap<String, LocalMsgCount> localMsgCountHashMapPerson = AppManager.getMsgCountList(IMType.MsgFromType.Person.toString());
        int groupUnRead = 0;

        try {
            Set<Map.Entry<String, LocalMsgCount>> groupEntries = localMsgCountHashMapGroup.entrySet();
            Iterator<Map.Entry<String, LocalMsgCount>> groupIterator = groupEntries.iterator();
            while (groupIterator.hasNext()) {
                Map.Entry entry = groupIterator.next();
                LocalMsgCount localMsgCount = (LocalMsgCount) entry.getValue();

                //tab红点只统计常用组未读消息数量  by cuizh,0407
//                if (localMsgCount != null) {
                if (localMsgCount != null && TextUtils.equals(localMsgCount.getIsDefault(),"1")) {

//                    LogUtil.i(tag,"localMsgCount test:  "+localMsgCount.getMsgId()+"    "+localMsgCount.getIsDefault());

                    int unRead = CommonUtils.toInt(localMsgCount.getMsgCount()) - CommonUtils.toInt(localMsgCount.getReadCount());
                    if (unRead <= 0) {
                        unRead = 0;
                    }
                    groupUnRead = groupUnRead + unRead;
                }
//                else {
//                    LogUtil.i(tag,"localMsgCount test:  "+localMsgCount.getMsgId()+"    "+localMsgCount.getIsDefault());
//                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        hashMap.put(IMType.MsgFromType.Group.toString(), groupUnRead);

        int personUnRead = 0;
        try {
            Set<Map.Entry<String, LocalMsgCount>> personEntries = localMsgCountHashMapPerson.entrySet();
            Iterator<Map.Entry<String, LocalMsgCount>> personIterator = personEntries.iterator();

            while (personIterator.hasNext()) {
                Map.Entry entry = personIterator.next();
                LocalMsgCount localMsgCount = (LocalMsgCount) entry.getValue();
                if (localMsgCount != null) {
                    int unRead = CommonUtils.toInt(localMsgCount.getMsgCount()) - CommonUtils.toInt(localMsgCount.getReadCount());
                    if (unRead <= 0) {
                        unRead = 0;
                    }
                    personUnRead = personUnRead + unRead;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        hashMap.put(IMType.MsgFromType.Person.toString(), personUnRead);
        return hashMap;
    }

    public static String getServerIpInOtherProcess() {
        String serverUrl = Configs.DEFAULT_SERVER_IP;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.aimissu.ptt/server");
            cursor = getApp().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {

                    String server = CommonUtils.emptyIfNull(cursor.getString(cursor.getColumnIndex(LocalServerDao.Properties.SERVER_IP.columnName))).trim();
                    LogUtil.i("MqttService", "getServerIpInOtherProcess:" + server);
                    if (!TextUtils.isEmpty(server)) {
                        serverUrl = server;
                    }
                    return serverUrl;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        LogUtil.i("MqttService", "getServerIpInOtherProcess default:" + serverUrl);
        return serverUrl;
    }

    public static String getVideoServerPort() {
        String serverUrl = Configs.DEFAULT_SERVER_PORT;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.aimissu.ptt/server");
            cursor = getApp().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String server = CommonUtils.emptyIfNull(cursor.getString(cursor.getColumnIndex(LocalServerDao.Properties.VIDEO_PORT.columnName))).trim();
                    LogUtil.i("MqttService", "getVideoServerInOtherProcess:" + server);
                    if (!TextUtils.isEmpty(server)) {
                        serverUrl = server;
                    }
                    return serverUrl;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        LogUtil.i("MqttService", "getVideoServerInOtherProcess default:" + serverUrl);
        return serverUrl;
    }

    public static String getVideoServerInOtherProcess() {
        return getServerIpInOtherProcess() + ":" + getVideoServerPort();
    }

    public static void setServer(String serverIp, String serverPort) {
        List<LocalServer> localServers = getDaoSession().getLocalServerDao().loadAll();
        LocalServer localServer;
        if (localServers != null && localServers.size() > 0) {
            localServer = localServers.get(0);
        } else {
            localServer = new LocalServer();
        }
        if (!TextUtils.isEmpty(serverIp)) {
            localServer.setSERVER_IP(serverIp);
        }
        if (!TextUtils.isEmpty(serverPort)) {
            localServer.setVIDEO_PORT(serverPort);
        }
        getDaoSession().getLocalServerDao().insertOrReplaceInTx(localServer);
    }

    public static boolean isNeedSedPwd() {
        return needSedPwd;
    }

    public static void setNeedSedPwd(boolean needSedPwd) {
        AppManager.needSedPwd = needSedPwd;
    }

    /**
     * 正常log日志上传api地址
     * @return
     */
    public static String getLogApi() {
        String serverUrl = Configs.DEFAULT_LOG_API;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.aimissu.ptt/logapi");
            cursor = getApp().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String server = CommonUtils.emptyIfNull(cursor.getString(cursor.getColumnIndex(LogConfigDao.Properties.ServerApi.columnName))).trim();
                    LogUtil.i("MqttService", "getVideoServerInOtherProcess:" + server);
                    if (!TextUtils.isEmpty(server)) {
                        serverUrl = server;
                    }
                    return serverUrl;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        LogUtil.i("MqttService", "getVideoServerInOtherProcess default:" + serverUrl);
        return serverUrl;
    }

    /**
     * 是否上传日志
     * @return
     */
    public static boolean getLogIsUpload() {
        boolean isUpload=true;
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.aimissu.ptt/logapi");
            cursor = getApp().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String server = CommonUtils.emptyIfNull(cursor.getString(cursor.getColumnIndex(LogConfigDao.Properties.IsUpload.columnName))).trim();
                    LogUtil.i("MqttService", "getLogIsUpload:" + server);
                    if (!TextUtils.isEmpty(server)) {
                        isUpload = server.toLowerCase().equals("true");
                    }
                    return isUpload;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isUpload;
    }
    public static  volatile SharePreferenceUtil sharePreferenceUtilThirdApp;
    public static  SharePreferenceUtil getThirdApp(){
        if (sharePreferenceUtilThirdApp == null) {
            synchronized (SharePreferenceUtil.class) {
                if (sharePreferenceUtilThirdApp == null) {
                    sharePreferenceUtilThirdApp =  new SharePreferenceUtil(getApp().getApplicationContext(),"ThirdAppInfo");;
                }
            }
        }
      return  sharePreferenceUtilThirdApp;
    }
    public static void setThirdAppLoginId(String loginId){
        getThirdApp().writeStringValue("loginId",loginId);
    }
    public static String getThirdAppLoginId(){
        return getThirdApp().getStringValue("loginId");
    }



    /**
     * 是否退出app,停止监听消息,空表示未知,true 表示停止了, false ,表示正在监听没有退出
     *
     * @return
     */
    public static boolean isStop() {
        LogUtil.i("MqttService", "isStop");
        String stopState = "";
        Cursor cursor = null;
        try {
            Uri uri = Uri.parse("content://com.aimissu.ptt/stop");
            cursor = getApp().getApplicationContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String isStop = CommonUtils.emptyIfNull(cursor.getString(cursor.getColumnIndex(LogConfigDao.Properties.IsStop.columnName))).trim();
                    LogUtil.i("MqttService", "AppManger.isStop :" + isStop);
                    if (!TextUtils.isEmpty(isStop)) {
                        stopState = isStop;
                    }
                    return "true".equals(stopState) ? true : false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        LogUtil.i("MqttService", "isStop default:" + stopState);
        return "true".equals(stopState) ? true : false;
    }

    public static void setStop(String state) {
        //开启服务器日志
        LogConfigDao logConfigDao = AppManager.getDaoSession().getLogConfigDao();
        List<LogConfig> logConfigDaos = logConfigDao.loadAll();
        LogConfig logConfig;
        if (logConfigDaos != null && logConfigDaos.size() > 0) {
            logConfig = logConfigDaos.get(0);
        } else {
            logConfig = new LogConfig();
        }
        logConfig.setIsStop(state);
        //更新到本地
        AppManager.getDaoSession().insertOrReplace(logConfig);
    }

}

