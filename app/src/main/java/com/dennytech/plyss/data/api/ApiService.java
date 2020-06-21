package com.dennytech.plyss.data.api;

import com.dennytech.plyss.data.model.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("states")
    Call<List<State>> getAllStates();
}
