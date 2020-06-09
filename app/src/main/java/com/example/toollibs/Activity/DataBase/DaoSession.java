package com.example.toollibs.Activity.DataBase;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.toollibs.Activity.Bean.Students;

import com.example.toollibs.Activity.DataBase.StudentsDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig studentsDaoConfig;

    private final StudentsDao studentsDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        studentsDaoConfig = daoConfigMap.get(StudentsDao.class).clone();
        studentsDaoConfig.initIdentityScope(type);

        studentsDao = new StudentsDao(studentsDaoConfig, this);

        registerDao(Students.class, studentsDao);
    }
    
    public void clear() {
        studentsDaoConfig.clearIdentityScope();
    }

    public StudentsDao getStudentsDao() {
        return studentsDao;
    }

}
