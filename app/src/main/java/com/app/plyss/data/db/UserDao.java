package com.app.plyss.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.app.plyss.data.model.User;

import java.util.List;

@Dao
public interface UserDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUsers(List<User> users);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addUser(User user);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("SELECT * FROM users")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM users WHERE email == :email LIMIT 1")
    User getUser(String email);

    @Delete
    void deleteUser(User user);
}
