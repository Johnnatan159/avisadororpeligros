package com.example.avisadororpeligros;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class IncidenciesInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Activity activity;

    public IncidenciesInfoWindowAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = activity.getLayoutInflater()
                .inflate(R.layout.info_window, null);

        Incidencia incidencia = (Incidencia) marker.getTag();

        ImageView ivProblema = view.findViewById(R.id.iv_problema);
        TextView tvProblema = view.findViewById(R.id.tvProblema);
        TextView tvDescripcio = view.findViewById(R.id.tvDescripcio);

        tvProblema.setText("Hora: " + incidencia.getHoraNotificacion());
        tvDescripcio.setText("Problema: " + incidencia.getProblema());
//        Glide.with(this.activity).load(Uri.parse(Uri.decode(incidencia.getUrl()))).into(ivProblema);
        Glide.with(this.activity).load(incidencia.getUrl()).into(ivProblema);

        return view;
    }
}