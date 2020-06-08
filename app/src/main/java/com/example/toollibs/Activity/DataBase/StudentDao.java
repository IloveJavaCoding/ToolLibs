package com.example.toollibs.Activity.DataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.example.toollibs.Activity.Bean.Students;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

public class StudentDao extends AbstractDao<Students, String> {
    //table name
    public static final String TABLE_NAME = "STUDENT";

    public StudentDao(DaoConfig config) {
        super(config);
    }

    public StudentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     *  properties of student table
     *  private String id;
     *  private String name;
     *  private String gender;//can be null
     *  private int age;//can be null
     *  private int grade;
     */
    public static class Properties{
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Gender = new Property(2, String.class, "gender", false, "GENDER");
        public final static Property Age = new Property(3, int.class, "age", false, "AGE");
        public final static Property Grade = new Property(4, int.class, "grade", false, "GRADE");
    }

    //create table
    public static void createTable(Database db, boolean isNotExisted){
        String constraint = isNotExisted? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STUDENT\" (" +
                "\"ID\" TEXT PRIMARY KEY NOT NULL ," +
                "\"NAME\" TEXT NOT NULL ," +
                "\"GENDER\" TEXT," +
                "\"AGE\" INTEGER," +
                "\"GRADE\" INTEGER NOT NULL );");
    }

    //drop table
    public static void dropTable(Database db, boolean isExists){
        String sql = "DROP TABLE " + (isExists? "IF EXISTS ":"") +"\"STUDENT\"";
        db.execSQL(sql);
    }

    @Override
    protected Students readEntity(Cursor cursor, int offset) {
        Students entity = new Students(
                cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
                cursor.getString(offset + 1), // name
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // gender
                cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // age
                cursor.getInt(offset + 4) // grade
        );
        return entity;
    }

    @Override
    protected String readKey(Cursor cursor, int offset) {//id: primary key
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }

    @Override
    protected void readEntity(Cursor cursor, Students entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.getString(offset + 1));
        entity.setGender(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAge(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setGrade(cursor.getInt(offset + 4));
    }

    @Override
    protected void bindValues(DatabaseStatement stmt, Students entity) {
        stmt.clearBindings();

        String id = entity.getId();
        if(id!=null){
            stmt.bindString(1, id);
        }

        stmt.bindString(2, entity.getName());

        String gender = entity.getGender();
        if(gender!=null){
            stmt.bindString(3, gender);
        }

        int age = entity.getAge();
        if(String.valueOf(age)!=null){
            stmt.bindLong(4, age);
        }

        stmt.bindLong(5, entity.getGrade());
    }

    @Override
    protected void bindValues(SQLiteStatement stmt, Students entity) {
        stmt.clearBindings();

        String id = entity.getId();
        if(id!=null){
            stmt.bindString(1, id);
        }

        stmt.bindString(2, entity.getName());

        String gender = entity.getGender();
        if(gender!=null){
            stmt.bindString(3, gender);
        }

        int age = entity.getAge();
        if(String.valueOf(age)!=null){
            stmt.bindLong(4, age);
        }

        stmt.bindLong(5, entity.getGrade());
    }

    @Override
    protected String updateKeyAfterInsert(Students entity, long rowId) {
        return entity.getId();//return primary key
    }

    @Override
    protected String getKey(Students entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    protected boolean hasKey(Students entity) {
        return entity.getId()!=null;
    }

    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }
}
