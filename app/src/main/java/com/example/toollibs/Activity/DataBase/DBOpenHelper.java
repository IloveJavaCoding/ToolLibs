package com.example.toollibs.Activity.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBOpenHelper extends DaoMaster.DevOpenHelper {
    //data base name
    public static final String DATABASE_NAME = "studentInfo.db";

    private Context context;
    private File dataBasePath;

    public DBOpenHelper(Context context, String name) {
        super(context, name);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
        this.context = context;
        dataBasePath = context.getDatabasePath(DATABASE_NAME);
    }

    /**
     * Check Database if it exists
     */
    private boolean databaseExists() {
        SQLiteDatabase sqliteDatabase = null;
        try {
            if (dataBasePath.exists()) {
                sqliteDatabase = SQLiteDatabase.openDatabase(dataBasePath.getAbsolutePath(), null,
                        SQLiteDatabase.OPEN_READWRITE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
        return sqliteDatabase != null;
    }

    // copy db
    private static int copy(InputStream in, OutputStream out)throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        while (true) {
            int read = in.read(buffer);
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, read);
            byteCount += read;
        }
        in.close();
        out.close();
        return byteCount;
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading schema from version " + oldVersion +
                " to " + newVersion);
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
