package com.example.turismotfg;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.Entity.Places;
import com.squareup.picasso.Picasso;

import java.io.IOException;
/*
Clase que representa el perfil de los lugares
mas emblemáticos representados en las guias turisticas
*/
public class PlaceProfile extends AppCompatActivity {
    private MediaPlayer reproductor;
    private SeekBar barra;
    Handler hand=new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_profile);

        TextView inputName = findViewById(R.id.inputName);
        TextView inputDescription = findViewById(R.id.inputDescription);
        LinearLayout inputView = findViewById(R.id.placeView);
        Button back_button = findViewById(R.id.button_back);
        Button play = findViewById(R.id.playButton);
        Button pause = findViewById(R.id.pauseButton);
        Button reset = findViewById(R.id.restartButton);
        barra=findViewById(R.id.audio_bar);

        Intent intent = getIntent();
        if (intent != null) {
            Places place = (Places) intent.getSerializableExtra("place");
            if (place != null) {
                inputName.setText(place.getNombre());
                inputDescription.setText(place.getDescripcion());
                for (String imageUrl : place.getImagenes()) {
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
                            Intent intent = new Intent(PlaceProfile.this, ZoomImageActivity.class);
                            intent.putExtra("imageUrl", imageUrl); // Pasa la URL de la imagen a la actividad de vista previa
                            startActivity(intent);
                        }
                    });
                    inputView.addView(imageView);
                }
                String audio=place.getAudioFile();
                if (audio!=null && !audio.isEmpty()){
                    reproductor=new MediaPlayer();
                    try {
                        reproductor.setDataSource(audio);
                        reproductor.prepare();
                        progressBar();
                    } catch (IOException e) {
                        e.printStackTrace(); // Imprimir el mensaje de error en la consola de registro
                    }
                }
            }
        }
        /*
            Funcion que se activa cuando el botón play actua se pulsa,
            esta función comprueba si el reproductar y existe, y si el reproductor
            no esta reproduciendose, y si cumple esas condiciones se inicia el
            reproductor
        */
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


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reproductor != null) {
                    reproductor.release();
                    reproductor=null;
                }
                finish();
            }
        });
    }

    private void progressBar() {
        if (reproductor!=null){
            barra.setMax(reproductor.getDuration());
            barra.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser && reproductor.isPlaying()){
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
                        int position=reproductor.getCurrentPosition();
                        barra.setProgress(position);
                    }
                    hand.postDelayed(this,1000);
                }
            },0);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reproductor != null) {
            reproductor.reset();
            reproductor=null;
        }
    }
}

