package com.dennytech.yorubacapture.data.repository;

import android.app.Application;

import com.dennytech.yorubacapture.data.db.AppDatabase;
import com.dennytech.yorubacapture.data.db.UserDao;
import com.dennytech.yorubacapture.data.model.User;

public class DataRepository {
    private AppDatabase database;
    private UserDao userDao;

    public DataRepository(Application application) {
        database = AppDatabase.getDatabase(application);
        userDao = database.userDao();
    }

    public void addUser(User user){
        userDao.addUser(user);
    }

    private void defaultUsers() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setName("Admin");

        userDao.addUser(user);
    }

    private void getUser(String id) {
        userDao.getUser(id);
    }
}
