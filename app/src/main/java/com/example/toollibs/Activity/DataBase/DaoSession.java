package com.example.toollibs.Activity.DataBase;

import com.example.toollibs.Activity.Bean.Students;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;
/**
 * need do all the ops for each created dao X7
 */
class DaoSession extends AbstractDaoSession {
    //dao config 1
    private final DaoConfig studentDaoConfig;

    //dao 2
    private final StudentDao studentDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?,?>>, DaoConfig> map) {
        super(db);
        //init config 3
        this.studentDaoConfig = map.get(StudentDao.class).clone();
        this.studentDaoConfig.initIdentityScope(type);

        //init dao 4
        this.studentDao = new StudentDao(studentDaoConfig, this);

        //register dao 5
        registerDao(Students.class, studentDao);//obj class, dao class
    }

    //clear IdentityScope 6
    public void clear(){
        studentDaoConfig.clearIdentityScope();
    }

    //get dao methods 7
    public StudentDao getStudentDao(){
        return studentDao;
    }
}
