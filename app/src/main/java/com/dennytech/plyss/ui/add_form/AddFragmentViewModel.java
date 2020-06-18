package com.dennytech.plyss.ui.add_form;

import android.app.Application;
import android.util.Patterns;

import androidx.lifecycle.ViewModel;

import com.dennytech.plyss.data.model.Form;
import com.dennytech.plyss.data.repository.DataRepository;

public class AddFragmentViewModel extends ViewModel {
    private DataRepository repository;

    public AddFragmentViewModel(Application application) {
        repository = new DataRepository(application);
    }

    public boolean userEmailValid(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void saveFormData(Form form) {
        repository.addDataCapture(form);
    }
}