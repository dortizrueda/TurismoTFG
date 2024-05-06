package com.example.turismotfg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.turismotfg.Managers.userManager;

public class EditNameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_name);

        Button edit_button=findViewById(R.id.btnEdit);
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText name=findViewById(R.id.name);
                String name1=name.getText().toString();
                Log.d("NAME",name1);
                userManager user=new userManager(EditNameActivity.this);
                user.editName(name1);
            }
        });
    }
}
