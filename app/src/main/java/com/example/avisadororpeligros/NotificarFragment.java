package com.example.avisadororpeligros;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class NotificarFragment extends Fragment {
    //TODO: Se deshabilita el progressBar
//    ProgressBar mLoading;
    private TextInputEditText txtLatitud;
    private TextInputEditText txtLongitud;
    //    private TextInputEditText txtDireccio;
    private TextInputEditText txtDescripcio;
    private TextInputEditText txtHora;
    private TextInputEditText txtCiudad;
    private Spinner spinnerLinieasMetro;
    private Spinner spinnerTipoIncidencia;

    private Button buttonNotificar;
    private SharedViewModel model;

    //Ruta on guardarem la fotografia.
    String mCurrentPhotoPath;
    //Ruta a la fotografia en format URI.
    private Uri photoURI;
    //Utilitzarem aquesta variable per accedir al ImageView del fragment.
    private ImageView foto;
    //Aquesta variable la utilitzarem en el mètode startActivityForResult().
    static final int REQUEST_TAKE_PHOTO = 1;

    String downloadUrl;
    private StorageReference storageRef;

    //TODO:Nuevo(De momento..)
    private String lineaMetroSeleccionada;
    private String tipoIncidenciaSeleccionada;

    public NotificarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificar, container, false);

//        mLoading = view.findViewById(R.id.loading);

        model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);

        txtLatitud =  view.findViewById(R.id.txtLatitud);
        txtLongitud = view.findViewById(R.id.txtLongitud);
//        txtDireccio = view.findViewById(R.id.txtDireccio);
        txtCiudad = view.findViewById(R.id.txtCiudad);
        txtHora = view.findViewById(R.id.txtHora);
        spinnerLinieasMetro = view.findViewById(R.id.spinnerLineasMetros);
        spinnerTipoIncidencia = view.findViewById(R.id.spinnerTipoProblema);
        txtDescripcio = view.findViewById(R.id.txtDescripcio);
        buttonNotificar = view.findViewById(R.id.buttonNotificar);

        //TODO:Nuevo - Lista de linieas de metro
        // String[] lineasMetroList={"Hospital de Bellvitge/Fondo","Paral·lel/Badalona Pompeu Fabra","Zona Universitària/Trinitat Nova","La Pau/Trinitat Nova","Cornellà Centre/Vall d'Hebron"};
        // int imagesMetroList[] = {R.drawable.ic_l1_barcelona,R.drawable.ic_l2_barcelona, R.drawable.ic_l3_barcelona, R.drawable.ic_l4_barcelona, R.drawable.ic_l5_barcelona};

        String[] lineasMetroList={"Dona","Home"};
        int imagesMetroList[] = {R.drawable.ic_launcher_dona,R.drawable.ic_launcher_home};


        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinnerLinieasMetro = (Spinner) view.findViewById(R.id.spinnerLineasMetros);
        spinnerLinieasMetro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You Select Position: "+position+" "+lineasMetroList[position], Toast.LENGTH_SHORT).show();
                lineaMetroSeleccionada = lineasMetroList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CustomAdapter customAdapterLinieasMetro=new CustomAdapter(getContext(),imagesMetroList,lineasMetroList);
        spinnerLinieasMetro.setAdapter(customAdapterLinieasMetro);

        //TODO: lista de incidencias
        String[] tiposIncidenciasList ={"Aviso de grafitero","Aviso de carterista","Aviso de alteración del orden"};
        int imagesIncidenciasList[] = {R.drawable.ic_spray,R.drawable.ic_criminal_stealing, R.drawable.ic_criminal_fighting};

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        spinnerTipoIncidencia = (Spinner) view.findViewById(R.id.spinnerTipoProblema);
        spinnerTipoIncidencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(), "You Select Position: "+position+" "+tiposIncidenciasList[position], Toast.LENGTH_SHORT).show();
                tipoIncidenciaSeleccionada = tiposIncidenciasList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CustomAdapter customAdapterTiposIncidencias=new CustomAdapter(getContext(),imagesIncidenciasList,tiposIncidenciasList);
        spinnerTipoIncidencia.setAdapter(customAdapterTiposIncidencias);

        //txtDireccio
        model.getCurrentAddress().observe(this, address -> {
//            txtCiudad.setText(getString(R.string.address_text,
//                    address, System.currentTimeMillis()));
           txtCiudad.setText(getString(R.string.locale_text, address));
           txtHora.setText(getString(R.string.hour_text, System.currentTimeMillis()));
        });


        model.getCurrentLatLng().observe(this, latlng -> {
            txtLatitud.setText(String.valueOf(latlng.latitude));
            txtLongitud.setText(String.valueOf(latlng.longitude));
        });

        //TODO: Se deshabilita el progressBar
        /*
        model.getProgressBar().observe(this, visible -> {
            if(visible)
                mLoading.setVisibility(ProgressBar.VISIBLE);
            else
                mLoading.setVisibility(ProgressBar.INVISIBLE);
        });*/

        model.switchTrackingLocation();

        //TODO:Nuevo
        String[] objects = {""};

        buttonNotificar.setOnClickListener(button -> {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();

            StorageReference imageRef = storageRef.child(mCurrentPhotoPath);
            UploadTask uploadTask = imageRef.putFile(photoURI);
            //Agreguem un Observer que es dispara quan acabi de pujar el fitxer.
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                //taskSnapshot.getMetadata() contindrà metadades sobre com el fitxer com la mida, el tipus, etc.
                imageRef.getDownloadUrl().addOnCompleteListener(task -> {
                    Uri downloadUri = task.getResult();
                    Glide.with(this).load(downloadUri).into(foto);

                    Log.e(null, downloadUri.toString());

                    downloadUrl = downloadUri.toString();


                    //Emplenem el POJO amb les dades del formulari.
                    Incidencia incidencia = new Incidencia();
//                    incidencia.setDireccio(txtDireccio.getText().toString());
                    incidencia.setLatitud(txtLatitud.getText().toString());
                    incidencia.setLongitud(txtLongitud.getText().toString());
                    incidencia.setProblema(txtDescripcio.getText().toString());
                    incidencia.setUrl(downloadUrl); // Guardem la url de la imatge pujada.
                    //TODO:Nuevo
                    incidencia.setLiniaMetro(lineaMetroSeleccionada);
                    incidencia.setTipoIncidencia(tipoIncidenciaSeleccionada);
                    incidencia.setHoraNotificacion(txtHora.getText().toString());

                    //Instanciem el sistema d’autenticació.
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    //Demanem una referència a la BD.
                    DatabaseReference base = FirebaseDatabase.getInstance().getReference();

                    //Naveguem a la part d’usuaris.
                    DatabaseReference users = base.child("users");
                    //Busquem l’ID de l’usuari autenticat.
                    DatabaseReference uid = users.child(auth.getUid());
                    //Naveguem a la branca per a les notificacions de l’usuari.
                    DatabaseReference incidencies = uid.child("incidencies");

                    //Creem una incidència nova.
                    DatabaseReference reference = incidencies.push();
                    //Emplenem en les dades de l’objecte
                    reference.setValue(incidencia);

                    Toast.makeText(getContext(), "Avís donat " + downloadUrl, Toast.LENGTH_SHORT).show();


                });
            });
        });

        foto = view.findViewById(R.id.foto);
        //TODO: Nuevo
        foto.setImageResource(R.drawable.ic_no_image_icon_1);
        Button buttonFoto = view.findViewById(R.id.buttonFotografiar);

        buttonFoto.setOnClickListener(button -> {
            dispatchTakePictureIntent();
        });

        return view;
    }


    private File createImageFile() throws IOException {
        //Creem un nom de fitxer.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                //Prefix.
                imageFileName,
                //Sufix.
                ".jpg",
                //Directori
                storageDir
        );
        //Guardem la imatge en la ruta completa.
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(
                //Verifiquem que hi ha alguna aplicació que pugui fer fotografies.
                getContext().getPackageManager()) != null) {
            //Creem el fitxer on es guardarà la foto.
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                //Gestionem els errors ocasionats.

            }

            //Continuem sols si no tenim errors.
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getContext(),
                        "com.project.benji.notificadorincidenciasmetro.provider",
                        photoFile);
                //Creem l’Intent per a fer la foto.
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                //Obrim la càmera del sistema. Al retornar s’executarà el mètode onActivityResult.
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Verifiquem que retornem des de la càmera.
        if (requestCode == REQUEST_TAKE_PHOTO) {
            //La foto s’ha realitzat correctament
            if (resultCode == Activity.RESULT_OK) {
                //Mostrem la imatge utilitzant Glide.

                Glide.with(this).load(photoURI).into(foto);



                //Aquest codi s’executarà si no s’ha fet la fotografia.
            } else {
                //Mostrem un missatge d’error.
                Toast.makeText(getContext(),
                        "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}