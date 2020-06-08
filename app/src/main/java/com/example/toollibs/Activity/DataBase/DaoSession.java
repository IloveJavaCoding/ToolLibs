package com.example.toollibs.Activity.DataBase;

import com.example.toollibs.Activity.Bean.Students;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;
/**
 * need do all the ops for each created dao
 */
class DaoSession extends AbstractDaoSession {
    //dao config
    private final DaoConfig studentDaoConfig;

    //dao
    private final StudentDao studentDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?,?>>, DaoConfig> map) {
        super(db);
        //init config
        this.studentDaoConfig = map.get(StudentDao.class).clone();
        this.studentDaoConfig.initIdentityScope(type);

        //init dao
        this.studentDao = new StudentDao(studentDaoConfig, this);

        //register dao
        registerDao(Students.class, studentDao);//obj class, dao class
    }

    //clear IdentityScope
    public void clear(){
        studentDaoConfig.clearIdentityScope();
    }

    //get dao methods
    public StudentDao getStudentDao(){
        return studentDao;
    }
}
