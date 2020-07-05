package com.app.plyss.ui.add_form;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddFragmentViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Application application;

    public AddFragmentViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddFragmentViewModel.class)) {
            return (T) new AddFragmentViewModel(application);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

}
