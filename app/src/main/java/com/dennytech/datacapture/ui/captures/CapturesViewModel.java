package com.dennytech.datacapture.ui.captures;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dennytech.datacapture.data.repository.DataRepository;

public class CapturesViewModel extends ViewModel {
    private DataRepository repository;

    public CapturesViewModel(Application application) {
        repository = new DataRepository(application);
    }


}