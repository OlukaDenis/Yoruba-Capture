package com.app.plyss.data.api;

import com.app.plyss.data.model.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("states")
    Call<List<State>> getAllStates();

    @GET("states/{state}/lgas")
    Call<List<String>> getStateLgas(@Path("state") String state);
}
