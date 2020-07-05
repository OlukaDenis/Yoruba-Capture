package com.app.plyss.ui.add_form;

import android.app.Application;
import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.app.plyss.data.model.Form;
import com.app.plyss.data.model.User;
import com.app.plyss.data.repository.DataRepository;

public class AddFragmentViewModel extends ViewModel {
    private DataRepository repository;

    public AddFragmentViewModel(Application application) {
        repository = new DataRepository(application);
    }

    public boolean userEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void saveFormData(Form form, String uid) {
        repository.addDataCapture(form, uid);
    }

    public User getUser(String email) {
        return repository.getUser(email);
    }
}