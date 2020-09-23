package com.nepalese.toollibs.Activity.DataBase;

import android.content.Context;

import com.nepalese.toollibs.Activity.Bean.Books;
import com.nepalese.toollibs.Activity.Bean.Students;

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
    private BooksDao booksDao;

    private DBHelper(Context context) {
        this.context = context;

        DaoSession session = getDaoSession(context);
        //init each dao 2
        studentsDao = session.getStudentsDao();
        booksDao = session.getBooksDao();
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
            DBOpenHelpers helper = new DBOpenHelpers(context, DBOpenHelpers.DATABASE_NAME, null);
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

    public boolean addBook(Books books){
        if(books.getName()!=null){
            booksDao.insert(books);
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

    public boolean deleteBook(Books books){
        if(books.getId()!=null){
            booksDao.delete(books);
            return true;
        }
        return false;
    }

    public boolean deleteBookByKey(Long id){
        booksDao.deleteByKey(id);
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

    public void updateBook(Books books){
        booksDao.update(books);
    }

    public void updateBook(Long id, String album){
        Books books = getBookByKey(id);
        books.setAlbum(album);

        booksDao.update(books);
    }

    public void updateBook(Long id, int tag){
        Books books = getBookByKey(id);
        books.setTag(tag);

        booksDao.update(books);
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

    public List<Books> getAllBook(){
        return booksDao.loadAll();
    }

    //get by key
    private Students getStudentByKey(Long id) {
        return studentsDao.load(id);
    }

    private Books getBookByKey(Long id) {
        return booksDao.load(id);
    }

    //judge exists or not by key
    public boolean idStudentExist(Long id){
        if(getStudentByKey(id)==null){
            return false;
        }
        return true;
    }
}
