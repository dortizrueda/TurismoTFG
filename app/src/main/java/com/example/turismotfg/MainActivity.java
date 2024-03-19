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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin, btnRegister;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate_email()==false | validate_password()==false) {
                    Toast.makeText(MainActivity.this, "Error en tus credenciales", Toast.LENGTH_SHORT).show();
                } else {
                    check_user();
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

    public boolean validate_email() {
        String email1 = editTextEmail.getText().toString();
        if (email1.isEmpty()) {
            editTextEmail.setError("No has introducido el correo electrónico.");
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }

    public boolean validate_password() {
        String password1 = editTextPassword.getText().toString();
        if (password1.isEmpty()) {
            editTextPassword.setError("No has introducido la contraseña.");
            return false;
        } else {
            editTextPassword.setError(null);
            return true;
        }
    }

    public void check_user() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
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
                                                            intent = new Intent(MainActivity.this, AdminActivity.class);
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
