package com.app.plyss.data.model;

import com.google.gson.annotations.SerializedName;

public class State {

    @SerializedName("name")
    private String name;


    public State() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}