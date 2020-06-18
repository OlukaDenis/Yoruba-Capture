package com.dennytech.datacapture.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dennytech.datacapture.R;
import com.dennytech.datacapture.data.model.Form;
import com.dennytech.datacapture.utils.AppGlobals;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dennytech.datacapture.utils.AppGlobals.CAPTURES;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private NavController navController;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseFirestore db;

    private Button open_form;
    @BindView(R.id.user_name)
    TextView userName;
    TextView capturedData;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModelFactory factory = new HomeViewModelFactory(requireActivity().getApplication());
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, root);
        db = FirebaseFirestore.getInstance();
        //Init firebase analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        mFirebaseAnalytics.setCurrentScreen(requireActivity(), this.getClass().getSimpleName(),
                this.getClass().getSimpleName());


        capturedData = root.findViewById(R.id.captured_data_number);

        statistics();

        open_form = root.findViewById(R.id.fab_add_data);
        open_form.setOnClickListener(v -> openAddForm());

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        userName.setText(AppGlobals.logged_in_user_email);
        return root;
    }

    private void openAddForm() {
        navController.navigate(R.id.navigation_add_form);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add new data form");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
    }

    private void statistics() {
        db.collection(CAPTURES).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                int count = Objects.requireNonNull(task.getResult()).size();
                Log.d("Captured Data: ", String.valueOf(count));
                capturedData.setText(String.valueOf(count));
            } else {
                Log.d(TAG, "Error getting captures: ", task.getException());
            }
        });
    }
}