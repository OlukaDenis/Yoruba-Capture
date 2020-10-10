package com.app.plyss;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

public class YorubaApp extends Application {
    public static YorubaApp mInstance;
    public FirebaseAnalytics mFirebaseAnalytics;
    public FirebaseAuth mAuth;
    public FirebaseFirestore db;
    public FirebaseUser currentUser;
    public FirebaseStorage mStorageRef;
    public FirebaseCrashlytics crashlytics;
    public PhoneAuthProvider phoneAuth;

    public static synchronized YorubaApp getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance();
        crashlytics = FirebaseCrashlytics.getInstance();
        phoneAuth = PhoneAuthProvider.getInstance();
        currentUser = mAuth.getCurrentUser();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setSslEnabled(true)
                .build();

        db.setFirestoreSettings(settings);

        mFirebaseAnalytics.setUserId(mAuth.getUid());
//        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
    }
}
