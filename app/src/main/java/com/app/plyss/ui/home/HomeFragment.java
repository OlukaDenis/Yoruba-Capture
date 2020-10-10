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
import com.app.plyss.utils.Vars;
import com.google.android.material.button.MaterialButton;
import com.google.errorprone.annotations.Var;
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

    private MaterialButton open_form;
    @BindView(R.id.user_name)
    TextView userName;

    @BindView(R.id.btnLoading)
    ProgressBar btnLoading;

    TextView individualData;
    private TextView householdData;
    private Vars vars;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModelFactory factory = new HomeViewModelFactory(requireActivity().getApplication());
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);
        vars = new Vars(requireActivity());

        vars.yorubaApp.crashlytics = FirebaseCrashlytics.getInstance();
        //Init firebase analytics
        vars.yorubaApp.mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        vars.yorubaApp.mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());


        individualData = root.findViewById(R.id.individual_data_number);
        householdData = root.findViewById(R.id.household_data_number);

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        if (vars.yorubaApp.currentUser != null) {
            userName.setText(vars.yorubaApp.currentUser.getEmail());
        }

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
        vars.yorubaApp.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }


    @OnClick(R.id.fab_household)
    void openHouseholdForm() {
        btnLoading.setVisibility(View.VISIBLE);
        navController.navigate(R.id.navigation_add_household);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add new household info");
        vars.yorubaApp.mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void statistics() {
        vars.yorubaApp.db.collection(INDIVIDUAL_CAPTURES).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = Objects.requireNonNull(task.getResult()).size();
                        Log.d("Captured Data: ", String.valueOf(count));
                        individualData.setText(String.valueOf(count));
                    } else {
                        vars.yorubaApp.crashlytics.recordException(Objects.requireNonNull(task.getException()));
                        Log.d(TAG, "Error getting captures: ", task.getException());
                    }
                })
                .addOnFailureListener(e ->  vars.yorubaApp.crashlytics.recordException(e));
    }

    private void householdStats() {
        vars.yorubaApp.db.collection(HOUSEHOLD_CAPTURES).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int count = Objects.requireNonNull(task.getResult()).size();
                        Log.d("Captured Data: ", String.valueOf(count));
                        householdData.setText(String.valueOf(count));
                    } else {
                        vars.yorubaApp.crashlytics.recordException(Objects.requireNonNull(task.getException()));
                        Log.e(TAG, "Error getting captures: ", task.getException());
                    }
                })
                .addOnFailureListener(e -> {
                    vars.yorubaApp.crashlytics.recordException(e);
                    Log.e(TAG, "householdStats: ",e );
                });
    }
}