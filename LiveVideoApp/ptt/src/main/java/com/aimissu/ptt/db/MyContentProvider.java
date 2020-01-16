package com.aimissu.ptt.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aimissu.ptt.entity.local.DaoMaster;
import com.aimissu.ptt.entity.local.DaoSession;
import com.aimissu.ptt.entity.local.LocalUserEntity;
import com.grgbanking.video.utils.LogUtil;

import org.greenrobot.greendao.database.Database;

/**
 */
public class MyContentProvider extends ContentProvider {
    public static final boolean ENCRYPTED = true;
    private Database db;
    private static final String AUTHORITY = "com.aimissu.ptt";
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private final static int USER = 1;
    private final static int SERVER = 2;
    private final static int LOGAPI = 3;
    private final static int ISSTOP = 4;
    DaoSession daoSession;

    static {
        URI_MATCHER.addURI(AUTHORITY, "user", USER);
        URI_MATCHER.addURI(AUTHORITY, "server", SERVER);
        URI_MATCHER.addURI(AUTHORITY, "logapi", LOGAPI);
        URI_MATCHER.addURI(AUTHORITY, "stop", ISSTOP);
    }

    @Override
    public boolean onCreate() {
        LogUtil.i("MqttService", "onCreate mycontent provider");
        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this.getContext(), ENCRYPTED ? "xqc-db-encrypted" : "xqc-db", null);
        db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        LogUtil.i("MqttService", "onCreate mycontent provider create successed");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        LogUtil.i("MqttService", "provider query");
        switch (URI_MATCHER.match(uri)) {
            case USER:
                LogUtil.i("MqttService", "get user");
                return daoSession.getLocalUserEntityDao().queryBuilder().buildCursor().query();
            case SERVER:
                LogUtil.i("MqttService", "get server");
                return daoSession.getLocalServerDao().queryBuilder().buildCursor().query();
            case LOGAPI:
                LogUtil.i("MqttService", "get login api");
                return daoSession.getLogConfigDao().queryBuilder().buildCursor().query();
            case ISSTOP:
                LogUtil.i("MqttService", "get logconfigdao is stop");
                return daoSession.getLogConfigDao().queryBuilder().buildCursor().query();
            default:
                break;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (URI_MATCHER.match(uri)) {
            case USER:
//                daoSession.getLocalUserEntityDao().deleteAll();
                LocalUserEntity localUserEntity = new LocalUserEntity();
                localUserEntity.setId(1l);
                localUserEntity.setLoginName("22222");
                daoSession.insertOrReplace(localUserEntity);
//                db.execSQL("insert into "+LocalUserEntityDao.TABLENAME+"( LOGIN_NAME) values('"+values+"')");
            default:
                break;
        }

        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case USER:
                daoSession.getLocalUserEntityDao().deleteAll();
            default:
                break;
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (URI_MATCHER.match(uri)) {
            case USER:
//                daoSession.getLocalUserEntityDao().update();
            default:
                break;
        }
        return 0;
    }
}
