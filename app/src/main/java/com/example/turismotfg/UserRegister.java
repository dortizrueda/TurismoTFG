package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.Entity.Rol;
import com.example.turismotfg.Entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity {

    private Button btnRegister, btnBack;

    private EditText  inputName, inputSurname, inputEmail, inputPassword;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;

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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString().trim();
                String surname = inputSurname.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                Rol rol = Rol.no_admin;

                // Validar la entrada del usuario
                if (checkRegister(name, surname, email, password)) {
                    // Crear usuario en Firebase Authentication
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(UserRegister.this, task -> {
                                if (task.isSuccessful()) {
                                    // Registro en Firebase Authentication exitoso
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                    if (firebaseUser != null) {
                                        // Crear objeto User
                                        User user = new User(email, name, surname, password,rol);

                                        // Obtener la referencia a la colección "users" en Firestore
                                        CollectionReference usersRef = FirebaseFirestore.getInstance().collection("users");

                                        // Guardar información en Firestore
                                        usersRef.document(firebaseUser.getUid()).set(user)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(UserRegister.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(UserRegister.this, MainActivity.class);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> {
                                                    // Manejar errores durante el registro en Firestore
                                                    Toast.makeText(UserRegister.this, "Error al registrar el usuario en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        // Manejar el caso en el que el objeto FirebaseUser sea nulo
                                        Toast.makeText(UserRegister.this, "Error: Usuario de Firebase nulo", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Manejar errores durante el registro en Firebase Authentication
                                    Toast.makeText(UserRegister.this, "Error al registrar el usuario en Firebase: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private boolean checkRegister(String name, String surname, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(surname) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(UserRegister.this, "Debes completar todos los registros.", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }
}

