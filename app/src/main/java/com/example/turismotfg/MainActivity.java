package com.example.turismotfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.turismotfg.Entity.Rol;
import com.example.turismotfg.Entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;

    private EditText input_email, input_password;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input_email = findViewById(R.id.email);
        input_password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email_checker()==false | password_checker()==false) {
                    Toast.makeText(MainActivity.this, "Error en tus credenciales", Toast.LENGTH_SHORT).show();
                } else {
                    user_checker();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserRegister.class));
            }
        });
    }

    public boolean email_checker() {
        String email1 = input_email.getText().toString();
        if (email1.isEmpty()) {
            input_email.setError("No has introducido el correo electrónico.");
            return false;
        } else {
            input_email.setError(null);
            return true;
        }
    }

    public boolean password_checker() {
        String password1 = input_password.getText().toString();
        if (password1.isEmpty()) {
            input_password.setError("No has introducido la contraseña.");
            return false;
        } else {
            input_password.setError(null);
            return true;
        }
    }

    public void user_checker() {
        String email = input_email.getText().toString().trim();
        String password = input_password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {//Listener que se va ejutar cuando se inicie sesión
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//Si Inicio Sesión fue exitoso
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            CollectionReference usersRef = db.collection("users");

                            usersRef.whereEqualTo("email", email)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    User user = document.toObject(User.class);

                                                    if (user != null) {
                                                        // Autenticación exitosa y se obtuvo la información del usuario
                                                        Intent intent;

                                                        if (TextUtils.equals(user.getRol().toString(), Rol.admin.toString())) {
                                                            // Usuario administrador, redirecciona a la actividad de administrador
                                                            intent = new Intent(MainActivity.this, MainActivity.class);
                                                        } else {
                                                            // No es administrador, redirecciona a la actividad de usuario normal
                                                            intent = new Intent(MainActivity.this, UserActivity.class);
                                                        }

                                                        // Pasa el objeto del usuario como extra al intent
                                                        intent.putExtra("USER_OBJECT_EXTRA", user);
                                                        startActivity(intent);
                                                    } else {
                                                        // El objeto User es nulo, manejar el caso según tus necesidades
                                                        Toast.makeText(MainActivity.this, "Error: Información del usuario nula", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } else {
                                                // Error al obtener datos de Firestore
                                                Toast.makeText(MainActivity.this, "Error al obtener información del usuario", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            // Autenticación fallida
                            Toast.makeText(MainActivity.this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
