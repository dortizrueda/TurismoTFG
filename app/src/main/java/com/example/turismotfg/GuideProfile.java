package com.example.turismotfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Managers.valoracionesManager;
import com.example.turismotfg.Entity.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuideProfile extends AppCompatActivity {
    Button back_button, button_maps, button_rating;
    RatingBar ratingBar, mediaBar;
    private MediaPlayer reproductor;
    private SeekBar barra;
    Handler hand = new Handler();
    List<Double> longitudes = new ArrayList<>(), latitudes = new ArrayList<>();
    List<String> names = new ArrayList<>();
    private RecyclerView recyclerViewPlace;
    private PlaceAdapter placeAdapter;
    valoracionesManager valoracionesManager;


    List<String> images = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_profile);

        back_button = findViewById(R.id.button_back);
        button_maps = findViewById(R.id.button_mapg);
        ratingBar = findViewById(R.id.guideValoration);
        button_rating = findViewById(R.id.button_rate);
        Button play = findViewById(R.id.playButton);
        Button pause = findViewById(R.id.pauseButton);
        Button reset = findViewById(R.id.restartButton);
        barra = findViewById(R.id.audio_bar);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        valoracionesManager val = new valoracionesManager(GuideProfile.this);

        LinearLayout inputView = findViewById(R.id.placeView);
        TextView inputName = findViewById(R.id.inputName);
        TextView inputDescription = findViewById(R.id.inputDescription);
        TextView inputCreator = findViewById(R.id.inputCreator);
        recyclerViewPlace = findViewById(R.id.recyclerViewPlaces);

        String description = null;
        Intent i = getIntent();
        if (i != null) {
            String name = i.getStringExtra("name");
            description = i.getStringExtra("description");
            String creator = i.getStringExtra("creator");
            String audio = i.getStringExtra("audioURL");
            List<Places> placeList = (List<Places>) i.getSerializableExtra("placeList");

            if (name != null) {
                val.getValoracionByGuideandUser(user_id, name, ratingBar);
                inputName.setText(name);
            }
            if (description != null) {
                inputDescription.setText(description);
            }
            if (audio != null && !audio.isEmpty()) {
                Log.d("AUDio",audio);
                reproductor = new MediaPlayer();
                try {
                    reproductor.setDataSource(audio);
                    reproductor.prepare();
                    progressBar();
                } catch (IOException e) {
                    e.printStackTrace(); // Imprimir el mensaje de error en la consola de registro
                }
            }
            if (creator != null) {
                getEmail(creator, inputCreator);
            }
            if (placeList != null && !placeList.isEmpty()) {
                for (Places p : placeList) {
                    double longitude = p.getLongitud();
                    double latitude = p.getLatitud();
                    String name_place = p.getNombre();
                    names.add(name_place);
                    longitudes.add(longitude);
                    latitudes.add(latitude);
                    //images.add(imagenes);

                    for (String imageUrl : p.getImagenes()) {
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams imageView1 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        imageView.setLayoutParams(imageView1);
                        Picasso.get().load(imageUrl).resize(1100, 750).into(imageView);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Acción a realizar cuando se hace clic en la imagen
                                Intent intent = new Intent(GuideProfile.this, ZoomImageActivity.class);
                                intent.putExtra("imageUrl", imageUrl); // Pasa la URL de la imagen a la actividad de vista previa
                                startActivity(intent);
                            }
                        });
                        inputView.addView(imageView);
                    }
                }
            }
            recyclerViewPlace.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            placeAdapter = new PlaceAdapter(this, placeList);
            recyclerViewPlace.setAdapter(placeAdapter);


            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reproductor != null) {
                        reproductor.reset();
                        reproductor=null;
                    }
                    finish();
                }
            });
            button_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float value = (ratingBar.getRating());
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String user_uid = user.getUid();
                    valoracionesManager valoraciones = new valoracionesManager(GuideProfile.this);
                    Intent intent = getIntent();
                    valoraciones.addValoracion(user_uid, name, value);
                }
            });
            button_maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GuideProfile.this, MapGuideView.class);
                    intent.putExtra("longitudes", (ArrayList<Double>) longitudes);
                    intent.putExtra("latitudes", (ArrayList<Double>) latitudes);
                    intent.putExtra("names", (ArrayList<String>) names);
                    startActivity(intent);
                }
            });
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reproductor!=null && !reproductor.isPlaying()){
                        reproductor.start();
                    }
                }
            });
        /*
            Funcion que se activa cuando el botón reset  se pulsa,
            esta función comprueba si el reproductor  existe, y si el reproductor
            no esta reproduciendose, resetea el audio y lo inicia, y si se encuentra reproduciendose
            lo resetea el audio.
        */
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reproductor!=null){
                        if (reproductor.isPlaying()){
                            reproductor.seekTo(0);
                        }else{
                            reproductor.seekTo(0);
                            reproductor.start();
                        }
                    }
                }
            });
        /*
            Funcion que se activa cuando el botón pause  se pulsa,
            esta función comprueba si el reproductor  existe, y si el reproductor
            esta reproduciendose,y si cumple los requisitos pausa el audio del
            reproductor.
        */
            pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (reproductor!=null && reproductor.isPlaying()){
                        reproductor.pause();
                    }
                }
            });

        }
        String finalDescription = description;
        inputDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GuideProfile.this);
                builder.setMessage(finalDescription)
                        .setTitle("Descripción completa")
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Cerrar el diálogo si se hace clic en "Cerrar"
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void getEmail(String creator, TextView inputCreator) {
        FirebaseFirestore.getInstance().collection("users").document(creator).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String userEmail = document.getString("email");
                        if (userEmail != null) {
                            inputCreator.setText(userEmail);
                        } else {
                            Log.d("GuideProfile", "El documento del usuario no contiene un correo electrónico.");
                        }
                    } else {
                        Log.d("GuideProfile", "No se encontró ningún documento para el UID del creador.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("GuideProfile", "Error al obtener el documento del usuario:", e);
                });
    }

    private void progressBar() {
        if (reproductor != null) {
            barra.setMax(reproductor.getDuration());
            barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && reproductor.isPlaying()) {
                        reproductor.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            hand.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (reproductor != null && reproductor.isPlaying()) {
                        int position = reproductor.getCurrentPosition();
                        barra.setProgress(position);
                    }
                    hand.postDelayed(this, 1000);
                }
            }, 0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GuideProfile", "onDestroy() called");
        if (reproductor != null) {
            reproductor.reset();
            reproductor=null;
        }
    }
}


