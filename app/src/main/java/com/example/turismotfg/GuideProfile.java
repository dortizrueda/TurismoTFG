package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.Entity.Places;
import com.squareup.picasso.Picasso;


import java.util.List;

public class GuideProfile extends AppCompatActivity {
    Button back_button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_profile);

        back_button=findViewById(R.id.button_back);
        LinearLayout inputView = findViewById(R.id.placeView);
        TextView inputName=findViewById(R.id.inputName);
        TextView inputDescription=findViewById(R.id.inputDescription);

        Intent intent = getIntent();
        if (intent!=null){
            String name=intent.getStringExtra("name");
            String description=intent.getStringExtra("description");
            List<Places> placeList = (List<Places>) intent.getSerializableExtra("placeList");


            if (name!=null){
                inputName.setText(name);
            }
            if (description!=null){
                inputDescription.setText(description);
            }
            if (placeList != null && !placeList.isEmpty()) {
                for (Places p : placeList) {
                    for (String imageUrl : p.getImagenes()) {
                        ImageView imageView = new ImageView(this);
                        LinearLayout.LayoutParams imageView1 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        imageView.setLayoutParams(imageView1);

                        Picasso.get().load(imageUrl).into(imageView);

                        inputView.addView(imageView);
                    }
                }

        }
      //Boton de vuelta a la página anterior --> user_Activity.xml
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
}
