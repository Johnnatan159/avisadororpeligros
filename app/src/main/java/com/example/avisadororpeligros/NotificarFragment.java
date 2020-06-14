package com.example.avisadororpeligros;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificarFragment extends Fragment {

    TextView mLocationTextView;
    ProgressBar mLoading;
    private Button button;
    private SharedViewModel model;

    public NotificarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificar, container, false);

        button = view.findViewById(R.id.button_location);
        mLocationTextView = view.findViewById(R.id.localitzacio);
        mLoading = view.findViewById(R.id.loading);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        model.getCurrentAddress().observe(this, address -> {
            mLocationTextView.setText(getString(R.string.address_text,
                    address, System.currentTimeMillis()));
        });

        model.getButtonText().observe(this, s -> button.setText(s));
        model.getProgressBar().observe(this, visible -> {
            if(visible)
                mLoading.setVisibility(ProgressBar.VISIBLE);
            else
                mLoading.setVisibility(ProgressBar.INVISIBLE);
        });

        button.setOnClickListener((View clickedView) -> model.switchTrackingLocation());

        return view;
    }
}