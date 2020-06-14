package com.example.avisadororpeligros;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LlistarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LlistarFragment extends Fragment {



    public LlistarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llistar, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference base = FirebaseDatabase.getInstance().getReference();

        DatabaseReference users = base.child("users");
//        String authUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(auth.getUid() != null) {
            DatabaseReference uid = users.child(auth.getUid());
            DatabaseReference incidencies = uid.child("incidencies");

            FirebaseListOptions<Incidencia> options = new FirebaseListOptions.Builder<Incidencia>()
                    .setQuery(incidencies, Incidencia.class)
                    .setLayout(R.layout.lv_incidencia_row)
                    .setLifecycleOwner(this)
                    .build();


            FirebaseListAdapter<Incidencia> adapter = new FirebaseListAdapter<Incidencia>(options) {
                @Override
                protected void populateView(View v, Incidencia model, int position) {
//                    TextView txtDescripcio = v.findViewById(R.id.txtDescripcio);
//                    TextView txtAdreca = v.findViewById(R.id.txtAdreca);
//
//                    txtDescripcio.setText(model.getProblema());
//                    txtAdreca.setText(model.getDireccio());

                    //TODO: Nuevo
                    TextView txtLiniaMetro = v.findViewById(R.id.txtLiniaMetro);
                    TextView txtTipoIncidencia = v.findViewById(R.id.txtTipoIncidencia);
                    TextView txtHoraNotificacion = v.findViewById(R.id.txtHoraNotificacion);
                    ImageView ivLiniaMetro = v.findViewById(R.id.ivLiniaMetro);
                    ImageView ivTipoIncidencia = v.findViewById(R.id.ivTipoIncidencia);

                    txtLiniaMetro.setText("Línia de metro: " + model.getLiniaMetro());
                    txtTipoIncidencia.setText("Tipus d'incidència: " + model.getTipoIncidencia());
                    txtHoraNotificacion.setText(model.getHoraNotificacion());
                    ivLiniaMetro.setImageResource(getAsignarImageLiniaMetro(model.getLiniaMetro()));
                    ivTipoIncidencia.setImageResource(getAsignarImageTipoIncidencia(model.getTipoIncidencia()));
                }
            };

            ListView lvIncidencies = view.findViewById(R.id.lvIncidencies);
            lvIncidencies.setAdapter(adapter);
        }
        return view;
    }

    private int getAsignarImageLiniaMetro(String lineaMetro){
        if("Hospital de Bellvitge/Fondo".equals(lineaMetro)){
            return R.drawable.ic_l1_barcelona;
        }else if("Paral·lel/Badalona Pompeu Fabra".equals(lineaMetro)){
            return R.drawable.ic_l2_barcelona;
        }else if("Zona Universitària/Trinitat Nova".equals(lineaMetro)){
            return R.drawable.ic_l3_barcelona;
        }else if("La Pau/Trinitat Nova".equals(lineaMetro)){
            return R.drawable.ic_l4_barcelona;
        }else if("Cornellà Centre/Vall d'Hebron".equals(lineaMetro)){
            return R.drawable.ic_l5_barcelona;
        }else{
            return 0;
        }
    }

    private int getAsignarImageTipoIncidencia(String tipoIncidencia){
        if("Aviso de grafitero".equals(tipoIncidencia)){
            return R.drawable.ic_spray;
        }else if("Aviso de carterista".equals(tipoIncidencia)){
            return R.drawable.ic_criminal_stealing;
        }else if("Aviso de alteración del orden".equals(tipoIncidencia)){
            return R.drawable.ic_criminal_fighting;
        }else{
            return 0;
        }
    }
}