package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserRegister extends AppCompatActivity {

    private EditText editTextUsername, editTextName, editTextSurname, editTextEmail, editTextPassword, editTextRol;
    private Button btnRegister, btnBack;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario_activity);

        editTextName = findViewById(R.id.name);
        editTextSurname = findViewById(R.id.surname);
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextUsername = findViewById(R.id.username);
        btnRegister = findViewById(R.id.btnRegister);
        btnBack = findViewById(R.id.btnBack);

        database = FirebaseDatabase.getInstance("https://turismouco-221d5-default-rtdb.europe-west1.firebasedatabase.app");
        reference = database.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String surname = editTextSurname.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                Rol rol = Rol.no_admin;
                String username = editTextUsername.getText().toString().trim();

                // Crear usuario en Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(UserRegister.this, task -> {
                            if (task.isSuccessful()) {
                                // Registro en Firebase Authentication exitoso
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                if (firebaseUser != null) {
                                    // Crear objeto User
                                    User user = new User(username, email, name, surname, password, rol);

                                    // Guardar información en la base de datos en tiempo real
                                    reference.child(username).setValue(user);

                                    Toast.makeText(UserRegister.this, "Registro exitoso!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserRegister.this, MainActivity.class);
                                    startActivity(intent);
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
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}

