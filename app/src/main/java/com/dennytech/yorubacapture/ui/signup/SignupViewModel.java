package com.dennytech.yorubacapture.ui.signup;

import android.app.Application;
import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.dennytech.yorubacapture.data.model.User;
import com.dennytech.yorubacapture.data.repository.DataRepository;

public class SignupViewModel extends ViewModel {
    private DataRepository repository;

    public SignupViewModel(Application application) {
        repository = new DataRepository(application);
    }

    public void addUser(User user) {
        repository.addUser(user);
    }

    public User existingUser(String email) {
        return repository.getUser(email);

    }

    public boolean isEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    public boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
