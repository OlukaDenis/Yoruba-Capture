package com.app.plyss.ui.captured_data;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.app.plyss.R;
import com.google.firebase.analytics.FirebaseAnalytics;

public class CapturesFragment extends Fragment {

    private CapturesViewModel capturesViewModel;
    private FirebaseAnalytics mFirebaseAnalytics;
    private NavController navController;
    private RelativeLayout openIndividual;
    private RelativeLayout openHousehold;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CaptureViewModelFactory factory = new CaptureViewModelFactory(requireActivity().getApplication());
        capturesViewModel = new ViewModelProvider(this, factory).get(CapturesViewModel.class);

        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);

        View root = inflater.inflate(R.layout.fragment_captures, container, false);

        openIndividual = root.findViewById(R.id.individual_data);
        openHousehold = root.findViewById(R.id.household_data);
        openIndividual.setOnClickListener(v -> openIndividualFragment());
        openIndividual.setOnClickListener(v -> openIndividualFragment());
        openHousehold.setOnClickListener(v -> openHouseholdFragment());
        return root;
    }

    private void openIndividualFragment() {
        navController.navigate(R.id.navigation_individual_data);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Individual data");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void openHouseholdFragment() {
        navController.navigate(R.id.navigation_household_data);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "View Household data");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }
}