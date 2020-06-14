package com.example.avisadororpeligros;

import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapaFragment extends Fragment {

    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 100;

    public MapaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mapa, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference base = FirebaseDatabase.getInstance().getReference();

        if(auth.getUid() != null) {
            DatabaseReference users = base.child("users");
            DatabaseReference uid = users.child(auth.getUid());
            DatabaseReference incidencies = uid.child("incidencies");

            SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

            mapFragment.getMapAsync(map -> {
                // Codi a executar quan el mapa s'acabi de carregar.
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_COARSE_LOCATION);
                }
                map.setMyLocationEnabled(true);
                MutableLiveData<LatLng> currentLatLng = model.getCurrentLatLng();
                LifecycleOwner owner = getViewLifecycleOwner();
                currentLatLng.observe(owner, latLng -> {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    map.animateCamera(cameraUpdate);
                    currentLatLng.removeObservers(owner);
                });

                incidencies.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Incidencia incidencia = dataSnapshot.getValue(Incidencia.class);

                        LatLng aux = new LatLng(
                                Double.valueOf(incidencia.getLatitud()),
                                Double.valueOf(incidencia.getLongitud())
                        );

                        IncidenciesInfoWindowAdapter customInfoWindow = new IncidenciesInfoWindowAdapter(
                                getActivity()
                        );

                        Marker marker = map.addMarker(new MarkerOptions()
                                .title(incidencia.getProblema())
                                .snippet(incidencia.getDireccio())
                                .icon(bitmapDescriptorFromVector(getContext(), getLiniaMetroIcon(incidencia))).position(aux));
                        marker.setTag(incidencia);

                        map.setInfoWindowAdapter(customInfoWindow);
                        try {
                            // Customize the styling of the base map using a JSON object defined
                            // in a raw resource files-pictures.
                            boolean success = map.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            getActivity(), R.raw.map_style));

                            if (!success) {
                                Log.e(null, "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e(null, "Can't find style. Error: ", e);
                        }

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            });
        }
        // Inflate the layout for this fragment
        return view;
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
/*
    private int getLiniaMetroIcon(Incidencia incidencia){
        return "Hospital de Bellvitge/Fondo".equals(incidencia.getLiniaMetro()) ? R.drawable.ic_l1_barcelona : "Paral·lel/Badalona Pompeu Fabra".equals(incidencia.getLiniaMetro()) ? R.drawable.ic_l2_barcelona : "Zona Universitària/Trinitat Nova".equals(incidencia.getLiniaMetro()) ? R.drawable.ic_l3_barcelona : "La Pau/Trinitat Nova".equals(incidencia.getLiniaMetro()) ? R.drawable.ic_l4_barcelona :  R.drawable.ic_l5_barcelona;
    }
*/
    private int getLiniaMetroIcon(Incidencia incidencia){
    return "Dona".equals(incidencia.getLiniaMetro()) ? R.drawable.ic_launcher_dona : "Home".equals(incidencia.getLiniaMetro()) ? R.drawable.ic_launcher_home : R.drawable.ic_launcher_home;
}

}