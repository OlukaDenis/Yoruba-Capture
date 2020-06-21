package com.dennytech.plyss.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AppUtils {
    public AppUtils() {
    }

    public static String currentTime() {
        DateFormat time = new SimpleDateFormat("HH:mm a", Locale.UK);
        Date date = new Date();
        return time.format(date);
    }

    public static String currentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK);
        Date date = new Date();
        return dateFormat.format(date);
    }

    //Check for available network
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    //Key generator
    public static String generateKey(){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.substring(0, Math.min(uuid.length(), 8));

        Date date= new Date();
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);

        String key = timestamp.toString() + uuid;
        return key.replaceAll("[-+.^:,]","");
    }

    //Util methods

    public static String formatNumber(long number){
        NumberFormat formatter = new DecimalFormat("#,###");
        double num = (double) number;
        return formatter.format(num);
    }

    public static void saveUserEmail(String email, Context context) {
        SharedPreferences idSharedPref = context.getSharedPreferences(AppGlobals.app_unique_prefs, Context.MODE_PRIVATE);

        if (!idSharedPref.contains(AppGlobals.user_email)) {
            SharedPreferences.Editor editor = idSharedPref.edit();
            editor.putString(AppGlobals.user_email, email);
            editor.apply();
        }
    }

    public static String getSignedUserEmail(Context context) {
        SharedPreferences idSharedPref = context.getSharedPreferences(AppGlobals.app_unique_prefs, Context.MODE_PRIVATE);
        return  idSharedPref.getString(AppGlobals.user_email, "");
    }
}
