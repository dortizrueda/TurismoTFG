package com.example.turismotfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turismotfg.Activities.UserActivity;
import com.example.turismotfg.Activities.UserRegister;
import com.example.turismotfg.Managers.userManager;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Clase que muestra la página inicial de la aplicación,
 * la cual corresponde .
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity {

    private EditText input_email, input_password;

    public static final String SHARE_PREFS="shared_prefs";
    FirebaseAuth firebaseAuth;
    /**
     * Método que se ejecuta al iniciar la actividad.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
        //Comprobación por si ya se encuentran almacenadas las credenciales en dicho dispositivo.
        boolean active=sharedPreferences.getBoolean("active",false);
        if (active){
            startActivity(new Intent(MainActivity.this, UserActivity.class));
            finish();
            return;
        }
        input_email = findViewById(R.id.email);
        input_password = findViewById(R.id.password);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        firebaseAuth= FirebaseAuth.getInstance();
        userManager user=new userManager(MainActivity.this);

        //Listener utilizado para loguearse en el sistema
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_checker()==false | password_checker()==false) {
                    Toast toast = Toast.makeText(MainActivity.this, "Error debes escribir en tus credenciales.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    String email = input_email.getText().toString().trim();
                    String password = input_password.getText().toString().trim();

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                        Toast toast = Toast.makeText(MainActivity.this, "Error durante el login: ",Toast.LENGTH_SHORT);
                        toast.show();
                        return;
                    }
                    user.login(email,password);
                }
            }
        });
        //Listener que inicializa la actividad del registro de usuario
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this, UserRegister.class);
                MainActivity.this.startActivity(i);
            }
        });
    }
    /**
     * Método que comprueba si se ha completado el email.
     *
     * @return  Boolean dependiendo si se ha completado o no.
     */
    public boolean email_checker() {
        String email1 = input_email.getText().toString();
        if (email1.isEmpty()) {
            input_email.setError("No has introducido el correo electrónico.");
            return false;
        } else {
            return true;
        }
    }
    /**
     * Método que comprueba si se ha completado la contraseña.
     *
     * @return  Boolean dependiendo si se ha completado o no.
     */
    public boolean password_checker() {
        String password1 = input_password.getText().toString();
        if (password1.isEmpty()) {
            input_password.setError("No has introducido la contraseña.");
            return false;
        } else {
            return true;
        }
    }

}
