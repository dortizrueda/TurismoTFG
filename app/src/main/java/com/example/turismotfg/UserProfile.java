package com.example.turismotfg;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfile extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        TextView detailUsernameTextView = findViewById(R.id.detailUsername);
        TextView detailEmailTextView = findViewById(R.id.detailEmail);

        Intent intent = getIntent();
        if (intent != null) {
            String email = intent.getStringExtra("email");

            if (email != null) {
                detailEmailTextView.setText(email);
            }
        }
    }
}

