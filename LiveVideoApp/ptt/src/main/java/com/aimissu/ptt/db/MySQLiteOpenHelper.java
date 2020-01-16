package com.aimissu.ptt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aimissu.ptt.entity.local.DaoMaster;
import com.aimissu.ptt.entity.local.LocalChatMsgListDao;
import com.aimissu.ptt.entity.local.LocalMsgCountDao;
import com.aimissu.ptt.entity.local.LocalMsgFile;
import com.aimissu.ptt.entity.local.LocalMsgFileDao;
import com.aimissu.ptt.entity.local.LocalPersonUserEntityDao;
import com.aimissu.ptt.entity.local.LocalUserEntityDao;
import com.aimissu.ptt.entity.local.LocalUserGroupDao;

import org.greenrobot.greendao.database.Database;

/**
 * 解决数据库升级的问题
 * 思路是：创建临时数据表
 */
public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, LocalUserEntityDao.class, LocalChatMsgListDao.class, LocalMsgCountDao.class, LocalMsgFileDao.class, LocalUserGroupDao.class,LocalPersonUserEntityDao.class);
    }
}