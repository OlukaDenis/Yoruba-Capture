package com.app.plyss.ui.home;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.app.plyss.data.model.User;
import com.app.plyss.data.repository.DataRepository;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private DataRepository repository;

    public HomeViewModel(Application application) {
      repository = new DataRepository(application);
    }

    public String capturedData() {
        return String.valueOf(repository.captureCount());
    }

    public void deleteUser(User user) {
        repository.deleteUser(user);
    }

    public User getUser(String email) {
        return repository.getUser(email);
    }

}