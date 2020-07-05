package com.app.plyss.ui.captured_data;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.app.plyss.data.repository.DataRepository;

public class CapturesViewModel extends ViewModel {
    private DataRepository repository;

    public CapturesViewModel(Application application) {
        repository = new DataRepository(application);
    }


}