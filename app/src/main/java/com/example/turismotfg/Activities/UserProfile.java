package com.example.turismotfg.Activities;

import static com.example.turismotfg.MainActivity.SHARE_PREFS;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.turismotfg.MainActivity;
import com.example.turismotfg.R;
import com.google.android.material.navigation.NavigationView;
/**
 * Clase que muestra la actividad del perfil del Usuario
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class UserProfile extends AppCompatActivity {
    /**
     * Método que se ejecuta al iniciar la actividad.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        TextView detailEmailTextView = findViewById(R.id.textViewEmail);
        TextView detailNameTextView = findViewById(R.id.textViewName);
        TextView detailSurnameTextView=findViewById(R.id.textViewSurname);
        TextView detailRolTextView=findViewById(R.id.textViewRol);
        TextView detailPasswordTextView=findViewById(R.id.textViewPassword);
        ImageButton edit_name=findViewById(R.id.editName);
        ImageButton edit_surname=findViewById(R.id.editSurname);
        ImageButton edit_email=findViewById(R.id.editPassword);
        ImageButton menu_icon=findViewById(R.id.menu_icon);

        //Listener que abre el menú lateral de la aplicación
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer=findViewById(R.id.container2);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        Button back_button=findViewById(R.id.button_back);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        //Listener de los botones del interior del menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    Intent intent = new Intent(UserProfile.this, UserActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_profile) {
                    Intent intent = new Intent(UserProfile.this, UserProfile.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_help) {
                    Intent intent = new Intent(UserProfile.this,HelpActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_favs) {
                    Intent intent = new Intent(UserProfile.this,GuideLikeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_logout) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("active", false);
                    editor.apply();
                    Intent i = new Intent(UserProfile.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        String surname = sharedPreferences.getString("surname", "");
        String email = sharedPreferences.getString("email", "");
        String rol = sharedPreferences.getString("rol", "");
        String password=sharedPreferences.getString("password","");

        String creadorType = "Creador de Guias";
        String userType = "Usuario";
        if ("admin".equals(rol)) {
            rol = creadorType;
        } else {
            rol = userType;
        }
        detailEmailTextView.setText(email);
        detailNameTextView.setText(name);
        detailSurnameTextView.setText(surname);
        detailRolTextView.setText(rol);
        detailPasswordTextView.setText(password);
        //Listener para volver a la pantalla anterior
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Listener que abre la actividad EditNameActivity
        edit_name.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, EditNameActivity.class);
                startActivity(intent);
            }
        });
        //Listener que abre la actividad EditSurnameActivity
        edit_surname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,EditSurnameActivity.class);
                startActivity(intent);
            }
        });
        //Listener que abre la actividad EditPasswordActivity
        edit_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserProfile.this,EditPaswordActivity.class);
                startActivity(intent);
            }
        });

    }
}

