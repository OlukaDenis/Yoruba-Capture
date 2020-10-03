package com.app.plyss.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.app.plyss.R;
import com.app.plyss.utils.AppGlobals;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.app.plyss.utils.AppGlobals.HOUSEHOLD_CAPTURES;
import static com.app.plyss.utils.AppGlobals.INDIVIDUAL_CAPTURES;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private NavController navController;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseFirestore db;
    private FirebaseCrashlytics crashlytics;

    private MaterialButton open_form;
    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.btnLoading)
    ProgressBar btnLoading;

    TextView individualData;
    private TextView householdData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModelFactory factory = new HomeViewModelFactory(requireActivity().getApplication());
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);
        db = FirebaseFirestore.getInstance();
        crashlytics = FirebaseCrashlytics.getInstance();
        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());


        individualData = root.findViewById(R.id.individual_data_number);
        householdData = root.findViewById(R.id.household_data_number);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        userName.setText(AppGlobals.logged_in_user_email);

        statistics();
        householdStats();
        return root;
    }

    @OnClick(R.id.fab_add_data)
    void openAddForm() {
        btnLoading.setVisibility(View.VISIBLE);
        navController.navigate(R.id.navigation_add_form);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add new data form");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }


    @OnClick(R.id.fab_household)
    void openHouseholdForm() {
        btnLoading.setVisibility(View.VISIBLE);
        navController.navigate(R.id.navigation_add_household);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add new household info");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void statistics() {
        db.collection(INDIVIDUAL_CAPTURES).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = Objects.requireNonNull(task.getResult()).size();
                        Log.d("Captured Data: ", String.valueOf(count));
                        individualData.setText(String.valueOf(count));
                    } else {
                        crashlytics.recordException(Objects.requireNonNull(task.getException()));
                        Log.d(TAG, "Error getting captures: ", task.getException());
                    }
                })
                .addOnFailureListener(e -> crashlytics.recordException(e));
    }

    private void householdStats() {
        db.collection(HOUSEHOLD_CAPTURES).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = Objects.requireNonNull(task.getResult()).size();
                        Log.d("Captured Data: ", String.valueOf(count));
                        householdData.setText(String.valueOf(count));
                    } else {
                        crashlytics.recordException(Objects.requireNonNull(task.getException()));
                        Log.e(TAG, "Error getting captures: ", task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    crashlytics.recordException(e);
                    Log.e(TAG, "householdStats: ",e );
                });
    }
}