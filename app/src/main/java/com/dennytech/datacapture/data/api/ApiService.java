package com.dennytech.datacapture.data.api;

import com.dennytech.datacapture.data.model.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("states")
    Call<List<State>> getAllStates();
}
