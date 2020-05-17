package com.dennytech.yorubacapture.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import android.app.Application;
import android.util.Patterns;

import com.dennytech.yorubacapture.data.model.User;
import com.dennytech.yorubacapture.data.repository.DataRepository;

import java.util.List;

public class LoginViewModel extends ViewModel {
    private DataRepository repository;

    public LoginViewModel(Application application) {
       repository = new DataRepository(application);
    }

    public User getUser(String email) {
        return repository.getUser(email);
    }

    // A placeholder username validation check
    public boolean isUserNameValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // A placeholder password validation check
    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public LiveData<List<User>> getAllUsers() {
        return repository.allUsers();
    }
}
