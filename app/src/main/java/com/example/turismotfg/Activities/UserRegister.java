package com.example.turismotfg.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.Managers.userManager;
import com.example.turismotfg.Entity.Rol;
import com.example.turismotfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
/**
 * Clase que muestra la actividad del registro de usuarios
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class UserRegister extends AppCompatActivity {

    private Button btnRegister, btnBack;

    private EditText  inputName, inputSurname, inputEmail, inputPassword;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    /**
     * Método que se ejecuta al iniciar la actividad.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario_activity);

        inputName = findViewById(R.id.name);
        inputSurname = findViewById(R.id.surname);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        firebaseAuth = FirebaseAuth.getInstance();
        userManager user=new userManager(UserRegister.this);
        //Listener para el botón de envío del registro.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String surname = inputSurname.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Rol rol = Rol.no_admin;

                boolean register=checkRegister(name,surname,email,password);
                // Validar la entrada del usuario
                if (register) {
                    user.register(email,name,surname,password,rol);
                }
            }
        });


        //Listener para el botón de finalizar la actividad y vuelve a página anterior.
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /**
     * Método que se ejecuta para comprobar que todos los campos han sido completados.
     *
     * @param name estado de la actividad.
     * @param surname apellido del formulario de registro.
     * @param email email del formulario de registro
     * @param password contraseña del formulario de registro
     *
     * @return Boolean, falso si no están todos los campos completados,
     *                  verdadero si están todos los campos completados
     */
    private boolean checkRegister(String name, String surname, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(UserRegister.this, "Debes completar todos los registros.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}

