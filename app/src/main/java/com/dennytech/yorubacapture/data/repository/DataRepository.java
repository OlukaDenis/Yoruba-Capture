package com.dennytech.yorubacapture.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.dennytech.yorubacapture.data.db.AppDatabase;
import com.dennytech.yorubacapture.data.db.UserDao;
import com.dennytech.yorubacapture.data.model.User;

import java.util.List;

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

    public User getUser(String email) {
        return userDao.getUser(email);
    }

    public void deleteUser(User user){
        userDao.deleteUser(user);
    }

    public LiveData<List<User>> allUsers() {
        return userDao.getAllUsers();
    }
}
