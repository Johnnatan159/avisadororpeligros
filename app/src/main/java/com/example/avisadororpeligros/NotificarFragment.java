package com.example.avisadororpeligros;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificarFragment extends Fragment {
    ProgressBar mLoading;
    private TextInputEditText txtLatitud;
    private TextInputEditText txtLongitud;
    private TextInputEditText txtDireccio;
    private TextInputEditText txtDescripcio;

    private Button buttonNotificar;
    private SharedViewModel model;

    public NotificarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificar, container, false);

        mLoading = view.findViewById(R.id.loading);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        txtLatitud =  view.findViewById(R.id.txtLatitud);
        txtLongitud = view.findViewById(R.id.txtLongitud);
        txtDireccio = view.findViewById(R.id.txtDireccio);
        txtDescripcio = view.findViewById(R.id.txtDescripcio);
        buttonNotificar = view.findViewById(R.id.button_notificar);


        model.getCurrentAddress().observe(this, address -> {
            txtDireccio.setText(getString(R.string.address_text,
                    address, System.currentTimeMillis()));
        });
        model.getCurrentLatLng().observe(this, latlng -> {
            txtLatitud.setText(String.valueOf(latlng.latitude));
            txtLongitud.setText(String.valueOf(latlng.longitude));
        });


        model.getProgressBar().observe(this, visible -> {
            if(visible)
                mLoading.setVisibility(ProgressBar.VISIBLE);
            else
                mLoading.setVisibility(ProgressBar.INVISIBLE);
        });

        model.switchTrackingLocation();


        buttonNotificar.setOnClickListener(button -> {
            Incidencia incidencia = new Incidencia();
            incidencia.setDireccio(txtDireccio.getText().toString());
            incidencia.setLatitud(txtLatitud.getText().toString());
            incidencia.setLongitud(txtLongitud.getText().toString());
            incidencia.setProblema(txtDescripcio.getText().toString());

            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference base = FirebaseDatabase.getInstance(
            ).getReference();

            DatabaseReference users = base.child("users");
            DatabaseReference uid = users.child(auth.getUid());
            DatabaseReference incidencies = uid.child("incidencies");

            DatabaseReference reference = incidencies.push();
            reference.setValue(incidencia);
        });

        return view;
    }
}