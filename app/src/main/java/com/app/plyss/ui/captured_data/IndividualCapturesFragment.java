package com.app.plyss.ui.captured_data;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.plyss.R;
import com.app.plyss.data.model.Form;
import com.app.plyss.ui.adapters.CaptureAdapter;
import com.app.plyss.utils.AppGlobals;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndividualCapturesFragment extends Fragment {
    private ShimmerRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private CaptureAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_individual_captures, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.recycler_individual);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        populateCaptures();

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void populateCaptures() {
        Query filterQuery = db.collection(AppGlobals.INDIVIDUAL_CAPTURES)
                .orderBy("date_of_capture", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Form> captureOptions = new FirestoreRecyclerOptions.Builder<Form>()
                .setLifecycleOwner(this)
                .setQuery(filterQuery, Form.class)
                .build();

        adapter = new CaptureAdapter(captureOptions);
        recyclerView.setAdapter(adapter);
    }
}

