package com.dennytech.datacapture.ui.captures;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CaptureViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;

    public CaptureViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CapturesViewModel.class)) {
            return (T) new CapturesViewModel(application);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}