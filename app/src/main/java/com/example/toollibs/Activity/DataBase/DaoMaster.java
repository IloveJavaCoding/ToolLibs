package com.example.toollibs.Activity.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.AbstractDaoMaster;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;
import org.greenrobot.greendao.identityscope.IdentityScopeType;

/**
 *  need do all the ops for each created dao X3
 */
public class DaoMaster extends AbstractDaoMaster {
    //data base version
    public static final int SCHEMA_VERSION = 1;

    //init and register dao
    public DaoMaster(Database db) {
        super(db, SCHEMA_VERSION);

        //1
        registerDaoClass(StudentDao.class);
    }

    //create all dao
    public static void createAllTables(Database db, boolean isNotExists){
        //2
        StudentDao.createTable(db, isNotExists);
    }

    //drop all dao
    public static void dropAllTables(Database db, boolean isExists){
        //3
        StudentDao.dropTable(db, isExists);
    }

    @Override
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }

    @Override
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }

    //data base helper
    public static abstract class OpenHelper extends DatabaseOpenHelper{
        public OpenHelper(Context context, String name) {
            super(context, name, SCHEMA_VERSION);
        }

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(Database db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }

    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static  class DevOpenHelper extends OpenHelper{
        public DevOpenHelper(Context context, String name) {
            super(context, name);
        }

        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(Database db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }
}
