package com.dennytech.plyss.ui.signup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignupViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Application application;

    public SignupViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SignupViewModel.class)) {
            return (T) new SignupViewModel(application);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
