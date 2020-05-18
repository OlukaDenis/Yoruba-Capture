package com.dennytech.datacapture.ui.captures;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dennytech.datacapture.R;
import com.dennytech.datacapture.data.model.Form;
import com.dennytech.datacapture.ui.adapters.CaptureAdapter;
import com.dennytech.datacapture.utils.AppGlobals;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CapturesFragment extends Fragment {

    private CapturesViewModel capturesViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private CaptureAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CaptureViewModelFactory factory = new CaptureViewModelFactory(requireActivity().getApplication());
        capturesViewModel = new ViewModelProvider(this, factory).get(CapturesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_captures, container, false);

        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.captures_recycler);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        populateCaptures();

        return root;
    }

    private void populateCaptures() {
        Query filterQuery = db.collection(AppGlobals.CAPTURES)
                .orderBy("date_of_capture", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Form> captureOptions = new FirestoreRecyclerOptions.Builder<Form>()
                .setLifecycleOwner(this)
                .setQuery(filterQuery, Form.class)
                .build();

        adapter = new CaptureAdapter(captureOptions);
        recyclerView.setAdapter(adapter);
    }
}