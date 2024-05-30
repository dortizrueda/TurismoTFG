package com.example.turismotfg.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.R;
import com.squareup.picasso.Picasso;

public class ZoomImageActivity extends AppCompatActivity {
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
