package com.app.plyss.ui.add_form;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.plyss.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHouseholdFragment extends Fragment {


    public AddHouseholdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_household, container, false);
    }

}
