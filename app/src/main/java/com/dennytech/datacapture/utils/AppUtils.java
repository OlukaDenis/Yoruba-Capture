package com.dennytech.datacapture.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
}
