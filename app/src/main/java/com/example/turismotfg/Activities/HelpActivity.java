package com.example.turismotfg.Activities;

import static com.example.turismotfg.MainActivity.SHARE_PREFS;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.turismotfg.MainActivity;
import com.example.turismotfg.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Clase que representa la actividad que muestra un Tutorial.
 * Esta actividad permite al usuario visualizar el tutorial de la aplicación.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class HelpActivity extends AppCompatActivity {
    /**
     * Método que se ejecuta al iniciar la página.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);

        TextView use=findViewById(R.id.guide_uses);
        TextView list=findViewById(R.id.guide_favsList);
        TextView valoration=findViewById(R.id.guide_valoration);
        TextView edit=findViewById(R.id.guide_editUser);
        ImageButton menu=findViewById(R.id.menu_icon);

        //Listener que abre el menú lateral
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer=findViewById(R.id.container);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigation_view);
        //Listener de los botones del interior del menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    Intent intent = new Intent(HelpActivity.this, UserActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_profile) {
                    Intent intent = new Intent(HelpActivity.this, UserProfile.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_favs) {
                    Intent intent = new Intent(HelpActivity.this,GuideLikeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_help) {
                    Intent intent=new Intent(HelpActivity.this,HelpActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_logout) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("active", false);
                    editor.apply();
                    Intent i = new Intent(HelpActivity.this, MainActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });
        String message = "1. Abre la aplicación y explora las opciones de navegación.\n\n" +
                "2. Selecciona una sección para ver las guías disponibles.\n\n" +
                "3. Consulta las guías disponibles y elige una que te interese.\n\n" +
                "4. Disfruta del contenido de la guía y sigue las instrucciones proporcionadas.\n\n" +
                "4.1. En el perfil de las guías, encontrarás imágenes de los lugares que vas a visitar, además de una breve descripción de la guía y un audio explicativo de manera general. Este audio proporcionará instrucciones y recomendaciones a seguir.\n\n" +
                "4.2. También podrás ver los lugares que se van a visitar. Al hacer clic en alguno de ellos, se abrirá un nuevo perfil con un audio que explicará detalladamente las curiosidades de dicho lugar de interés, así como otra información relevante.\n\n";
        String message1="1.En el menu de Guides, puedes observar todas las guias disponibles en la aplicación y un buscador para poder buscar dichas guías por nombre.\n\n" +
                        "2. En dicho menu cada guia tiene un icono de guardado, el cual te permite almacenar esa guía como una de tus guías favoritas\n\n"+
                        "3. Para visualizar dichas guias favoritas debes ir al menú lateral seleccionar el icono de Guides Favs ";
        String message2="1.Abre la aplicación y entra en el perfil de alguna las Guias.\n\n"+
                "2.Encontrarás un Barra de Valoracion de 0-5 estrellas en el que puedes valorar dicha guía.\n\n";
        String message3="1.Abre el menu lateral, y accede al Profile.\n\n"+
                "2. Aparecerán toda tu información personal, la cual podrás editar pulsando en el icono de un lápiz.";

        //Listener para mostrar mensaje de Funcionamiento de Guías
        use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this);
                builder.setMessage(message)
                        .setTitle("Funcionamiento de Guias")
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //Listener para mostrar mensaje de Guías Favoritas
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this);
                    builder.setMessage(message1)
                            .setTitle("Guias Favoritas")
                            .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //Listener para mostrar mensaje de Valoración de Guias
        valoration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this);
                builder.setMessage(message2)
                        .setTitle("Valoración de Guías")
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        //Listener para mostrar mensaje de Editar información
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpActivity.this);
                builder.setMessage(message3)
                        .setTitle("Editar Datos")
                        .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}
