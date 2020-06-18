package com.dennytech.plyss.data.local;

import java.util.ArrayList;
import java.util.List;

public class LocalDataSource {
    private static final String TAG = "LocalDataSource";

    public LocalDataSource() {
    }

    public static List<String> ALL_STATES = new ArrayList<>();

    public static final  String[] GENDER = new String[] {"Male", "Female", "Prefer not to say"};

    public static final  String[] MARITAL_STATUS = new String[] {"Single", "Married", "Divorced", "Prefer not to say"};
}
