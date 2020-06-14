package com.example.avisadororpeligros;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LlistarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LlistarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LlistarFragment newInstance(String param1, String param2) {
        LlistarFragment fragment = new LlistarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_llistar, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference base = FirebaseDatabase.getInstance().getReference();

        DatabaseReference users = base.child("users");
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
                TextView txtDescripcio = v.findViewById(R.id.txtDescripcio);
                TextView txtAdreca = v.findViewById(R.id.txtAdreca);

                txtDescripcio.setText(model.getProblema());
                txtAdreca.setText(model.getDireccio());
            }
        };

        ListView lvIncidencies = view.findViewById(R.id.lvIncidencies);
        lvIncidencies.setAdapter(adapter);


        return view;
    }
}