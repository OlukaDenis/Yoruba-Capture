package com.app.plyss.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.plyss.YorubaApp;

public class Vars {
    public Context context;
    public YorubaApp yorubaApp;
    private SharedPreferences idSharedPref;

    public Vars(Context context) {
        this.context = context;
        yorubaApp = (YorubaApp) context.getApplicationContext();
        idSharedPref = context.getSharedPreferences(AppGlobals.app_unique_prefs, Context.MODE_PRIVATE);
    }

    public void saveUserEmail(String email) {
        if (!idSharedPref.contains(AppGlobals.user_email)) {
            SharedPreferences.Editor editor = idSharedPref.edit();
            editor.putString(AppGlobals.user_email, email);
            editor.apply();
        }
    }

    public String getSignedUserEmail() {
        return  idSharedPref.getString(AppGlobals.user_email, "");
    }

}
