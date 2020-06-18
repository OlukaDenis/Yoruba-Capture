package com.dennytech.plyss.ui.captures;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.dennytech.plyss.data.repository.DataRepository;

public class CapturesViewModel extends ViewModel {
    private DataRepository repository;

    public CapturesViewModel(Application application) {
        repository = new DataRepository(application);
    }


}