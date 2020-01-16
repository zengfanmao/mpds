package com.aimissu.ptt.view.widget.audio;

import com.aimissu.ptt.config.AppManager;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.local.LocalMsgFileDao;
import com.aimissu.ptt.utils.CommonUtils;

/**
 */
public class FileUtils {

    public static boolean isExitFile(String msgId) {
        LocalMsgFile localMsgFile = getLocalMsgFileById(CommonUtils.emptyIfNull(msgId).trim());
        return localMsgFile != null ? true : false;
    }

    public static LocalMsgFile getLocalMsgFileById(String msgId) {
        LocalMsgFileDao localMsgFileDao = AppManager.getDaoSession().getLocalMsgFileDao();
        LocalMsgFile localMsgFile = localMsgFileDao.queryBuilder().where(LocalMsgFileDao.Properties.MsgId.eq(CommonUtils.emptyIfNull(msgId).trim())).unique();
        return localMsgFile;
    }

    public static LocalMsgFile getLocalMsgFileByLocalUrl(String localUrl) {
        LocalMsgFileDao localMsgFileDao = AppManager.getDaoSession().getLocalMsgFileDao();
        LocalMsgFile localMsgFile = localMsgFileDao.queryBuilder().where(LocalMsgFileDao.Properties.LocalFileUrl.eq(CommonUtils.emptyIfNull(localUrl).trim())).unique();
        return localMsgFile;
    }

    public static LocalMsgFile getLocalMsgFileByFileCode(String fileCode) {
        LocalMsgFileDao localMsgFileDao = AppManager.getDaoSession().getLocalMsgFileDao();
        LocalMsgFile localMsgFile = null;
        try {
            localMsgFile = localMsgFileDao.queryBuilder().where(LocalMsgFileDao.Properties.FCode.eq(CommonUtils.emptyIfNull(fileCode).trim())).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localMsgFile;
    }

    public static LocalMsgFile getLocalMsgFileByMsgId(String msgId) {
        LocalMsgFileDao localMsgFileDao = AppManager.getDaoSession().getLocalMsgFileDao();
        LocalMsgFile localMsgFile = null;
        try {
            localMsgFile = localMsgFileDao.queryBuilder().where(LocalMsgFileDao.Properties.MsgId.eq(CommonUtils.emptyIfNull(msgId).trim())).unique();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localMsgFile;
    }

    public static LocalMsgFile getLocalMsgFileByDownloadUrl(String donwloadUrl) {
        LocalMsgFileDao localMsgFileDao = AppManager.getDaoSession().getLocalMsgFileDao();
        LocalMsgFile localMsgFile = localMsgFileDao.queryBuilder().where(LocalMsgFileDao.Properties.FileDownloadUrll.eq(CommonUtils.emptyIfNull(donwloadUrl).trim())).unique();
        return localMsgFile;
    }

    public static void addFile(LocalMsgFile localMsgFile) {
        if (localMsgFile == null)
            return;
        LocalMsgFileDao localMsgFileDao = AppManager.getDaoSession().getLocalMsgFileDao();
        LocalMsgFile localFile = getLocalMsgFileById(localMsgFile.msgId);
        if (localFile != null) {
            localMsgFileDao.delete(localFile);
        }
        localMsgFileDao.insertOrReplace(localMsgFile);
    }


}

