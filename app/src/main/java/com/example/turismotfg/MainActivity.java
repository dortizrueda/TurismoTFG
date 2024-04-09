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

import com.example.turismotfg.DAO.userDAO;
import com.google.firebase.auth.FirebaseAuth;
public class MainActivity extends AppCompatActivity {

    private EditText input_email, input_password;

    public static final String SHARE_PREFS="shared_prefs";
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
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
        userDAO user=new userDAO(MainActivity.this);


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
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,UserRegister.class);
                MainActivity.this.startActivity(i);
            }
        });
    }

    public boolean email_checker() {
        String email1 = input_email.getText().toString();
        if (email1.isEmpty()) {
            input_email.setError("No has introducido el correo electrónico.");
            return false;
        } else {
            return true;
        }
    }

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
