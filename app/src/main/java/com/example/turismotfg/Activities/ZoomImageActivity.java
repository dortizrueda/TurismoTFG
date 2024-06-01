package com.example.turismotfg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.R;
import com.squareup.picasso.Picasso;
/**
 * Clase que muestra las imágenes para verlas en su resolución correspondiente.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class ZoomImageActivity extends AppCompatActivity {
    /**
     * Método que se ejecuta al iniciar la actividad.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zoom_image);

        Intent i = getIntent();
        if (i!=null){
            String imageUrl = getIntent().getStringExtra("imageUrl");
            if (imageUrl!=null){
                ImageView imageView = findViewById(R.id.imageView);
                Picasso.get().load(imageUrl).into(imageView);
            }
        }
    }
}
