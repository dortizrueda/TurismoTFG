package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private ArrayList<User> userList;
    private UserAdapter userAdapter;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ValueEventListener eventListener;
    SearchView searchView;

    // Agrega las referencias de Firebase
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(AdminActivity.this, userList);  // Pasa el contexto al adaptador
        recyclerView.setAdapter(userAdapter);

        databaseReference = FirebaseDatabase.getInstance("https://turismouco-221d5-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("users");
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    User user = itemSnapshot.getValue(User.class);
                    userList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });

        // Agrega un OnClickListener al FAB (FloatingActionButton)
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Aquí puedes agregar la lógica para abrir la actividad de creación de usuario, por ejemplo.
            }
        });

        // Agrega un OnClickListener al RecyclerView
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Obtén el usuario seleccionado
                User selectedUser = userList.get(position);

                // Crea un Intent para abrir la UserProfileActivity
                Intent intent = new Intent(AdminActivity.this, UserProfile.class);

                // Pasa la información del usuario a la UserProfileActivity
                intent.putExtra("email", selectedUser.getEmail());

                // Inicia la UserProfileActivity
                startActivity(intent);
            }
        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Limpia el ValueEventListener al destruir la actividad para evitar fugas de memoria
        if (eventListener != null) {
            databaseReference.removeEventListener(eventListener);
        }
    }
}
