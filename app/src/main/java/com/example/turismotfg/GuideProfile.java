package com.example.turismotfg;

import static com.example.turismotfg.MainActivity.SHARE_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Entity.Places;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class GuideProfile extends AppCompatActivity {
    Button back_button;
    Button button_maps;
    List<Double> longitudes = new ArrayList<>(),latitudes=new ArrayList<>();
    List<String> names=new ArrayList<>();
    private RecyclerView recyclerViewPlace;
    private PlaceAdapter placeAdapter;


    List<String>images=new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_profile);

        back_button=findViewById(R.id.button_back);
        button_maps=findViewById(R.id.button_mapg);
        LinearLayout inputView = findViewById(R.id.placeView);
        TextView inputName=findViewById(R.id.inputName);
        TextView inputDescription=findViewById(R.id.inputDescription);
        recyclerViewPlace = findViewById(R.id.recyclerViewPlaces);

        Intent i = getIntent();
        if (i!=null){
            String name=i.getStringExtra("name");
            String description=i.getStringExtra("description");
            List<Places> placeList = (List<Places>) i.getSerializableExtra("placeList");


            if (name!=null){
                inputName.setText(name);
            }
            if (description!=null){
                inputDescription.setText(description);
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

            recyclerViewPlace.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false));
            placeAdapter = new PlaceAdapter(this,placeList);
            recyclerViewPlace.setAdapter(placeAdapter);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    }
}
}
