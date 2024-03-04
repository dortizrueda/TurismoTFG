package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        // Puedes agregar aquí el código específico para la actividad del administrador
        // Por ejemplo, configurar vistas, manejar eventos, etc.
        Intent intent = getIntent();
        if (intent.hasExtra("USER_OBJECT_EXTRA")){
            User user = (User) intent.getSerializableExtra("USER_OBJECT_EXTRA");
            if (user != null && user.getName()!=null){
                TextView welcomeTextView = findViewById(R.id.textWelcome);
                welcomeTextView.setText("¡Bienvenido de nuevo, " + user.getName() + "!");
            }
        }
    }
}
