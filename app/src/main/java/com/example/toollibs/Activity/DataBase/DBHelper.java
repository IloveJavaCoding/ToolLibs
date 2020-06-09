package com.example.toollibs.Activity.DataBase;

import android.content.Context;

import com.example.toollibs.Activity.Bean.Students;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 *  need do all the ops for each created dao X2
 */
public class DBHelper {
    private Context context;
    private static volatile DBHelper instance;

    private DaoSession daoSession;
    private DaoMaster daoMaster;

    //each dao 1
    private StudentsDao studentsDao;

    private DBHelper(Context context) {
        this.context = context;

        DaoSession session = getDaoSession(context);
        //init each dao 2
        studentsDao = session.getStudentsDao();
    }

    private DaoSession getDaoSession(Context context) {
        if(daoSession == null){
            daoMaster = getDaoMaster(context);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private DaoMaster getDaoMaster(Context context) {
        if(daoMaster == null){
            DBOpenHelper helper = new DBOpenHelper(context, DBOpenHelper.DATABASE_NAME, null);
            daoMaster = new DaoMaster(helper.getWritableDb());
        }
        return daoMaster;
    }

    public static DBHelper getInstance(Context context){
        if(instance==null){
            synchronized (DBHelper.class){
                if(instance==null){
                    instance = new DBHelper(context);
                }
            }
        }
        return instance;
    }

    //=========ops for each dao==========
    //insert/add
    public boolean addStudent(Students students){
        if(students.getStudentId()!=null){
            studentsDao.insert(students);
            return true;
        }
        return false;
    }

    //delete/move
    public boolean deleteStudent(Students students){
        if(students.getId()!=null){
            studentsDao.delete(students);
            return true;
        }
        return false;
    }

    public boolean deleteStudentByKey(Long id){
        studentsDao.deleteByKey(id);
        return true;
    }

    //update base primary key
    public void updateStudent1(Long id, String gender, int age){
        Students students = getStudentByKey(id);
        students.setGender(gender);
        students.setAge(age);

        studentsDao.update(students);
    }

    public void updateStudent2(Students students){
        studentsDao.update(students);
    }

    //list/query
    public List<Students> getAllStudents(){
        QueryBuilder<Students> qb = studentsDao.queryBuilder();
        return qb.build().list();
    }

    public List<Students> getAllStudents2(){
        return studentsDao.loadAll();
    }

    public List<Students> getStudentByGender(String gender){
        QueryBuilder<Students> qb = studentsDao.queryBuilder();
        qb.where(StudentsDao.Properties.Gender.eq(gender));
        return qb.build().list();
    }

    //get by key
    private Students getStudentByKey(Long id) {
        return studentsDao.load(id);
    }

    //judge exists or not by key
    public boolean idStudentExist(Long id){
        if(getStudentByKey(id)==null){
            return false;
        }
        return true;
    }
}
