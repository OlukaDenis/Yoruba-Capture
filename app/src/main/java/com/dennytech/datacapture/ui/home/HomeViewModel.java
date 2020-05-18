package com.dennytech.datacapture.ui.home;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dennytech.datacapture.data.model.Form;
import com.dennytech.datacapture.data.repository.DataRepository;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private static final String TAG = "HomeViewModel";
    private DataRepository repository;

    public HomeViewModel(Application application) {
      repository = new DataRepository(application);
    }

    public String capturedData() {
        return String.valueOf(repository.captureCount());


//        List<Form> data = repository.getAllCaptures();
//        Log.d(TAG, "capturedData: "+data.size());
//
//        if (data == null) {
//            return "0";
//        } else {
//            return String.valueOf(data.size());
//        }

    }

}