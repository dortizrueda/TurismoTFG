package com.example.turismotfg;

import static com.example.turismotfg.MainActivity.SHARE_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Entity.Places;
import com.example.turismotfg.Entity.User;
import com.example.turismotfg.Entity.Guide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity {

    private ArrayList<User> userList;
    private UserAdapter userAdapter;
    private ArrayList<Guide> guideList;
    private ArrayList<Places> placeList;
    private GuideAdapter guideAdapter;
    private FloatingActionButton fab;
    private RecyclerView recyclerView,recyclerViewGuide;

    private SearchView searchView;

    private FirebaseFirestore firestore;
    private CollectionReference usersReference,guideReference;
    private ImageButton mapButton,guideButton,button_logout,menu_icon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewGuide = findViewById(R.id.recyclerViewGuide);
        mapButton = findViewById(R.id.gps_button);
        guideButton=findViewById(R.id.guide_button);
        button_logout=findViewById(R.id.button_logout);
        menu_icon=findViewById(R.id.menu_icon);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(userAdapter);

        guideList = new ArrayList<>();
        placeList=new ArrayList<>();
        guideAdapter = new GuideAdapter(this, guideList);
        recyclerViewGuide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGuide.setAdapter(guideAdapter);

        AlertDialog dialog = createProgressDialog();

        firestore = FirebaseFirestore.getInstance();
        usersReference = firestore.collection("users");
        guideReference = firestore.collection("guides");
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    if (id == R.id.navigation_home) {
                        Intent intent = new Intent(UserActivity.this, UserActivity.class);
                        startActivity(intent); 
                    } else if (id == R.id.navigation_profile) {
                        Intent intent = new Intent(UserActivity.this, UserProfile.class);
                        startActivity(intent); 
                    } else if (id == R.id.navigation_logout) {
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("active", false);
                        editor.apply();
                        Intent i = new Intent(UserActivity.this, MainActivity.class);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(i);
                        finish();
                    }
                return true;
                }
        });

        dialog.show();
        usersReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error != null) {
                dialog.dismiss();
                return;
            }

            userList.clear();
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                User user = documentSnapshot.toObject(User.class);
                userList.add(user);
            }
            userAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        guideReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            if (error!=null){
                dialog.dismiss();
                return;
            }
            guideList.clear();
            for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()){
                DocumentSnapshot documentSnapshot = documentChange.getDocument();
                Guide guide = documentSnapshot.toObject(Guide.class);
                guideList.add(guide);
            }
            guideAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        //Inicializar Mapa
        mapButton.setOnClickListener(v -> startActivity(new Intent(UserActivity.this, MapView.class)));
        guideButton.setOnClickListener(v -> startActivity(new Intent(this,GuideActivity.class)));
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("active", false);
                editor.apply();
                Intent i = new Intent(UserActivity.this, MainActivity.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(i);
                finish();
            }
        });
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer=findViewById(R.id.container);
                drawer.openDrawer(GravityCompat.START);
            }
        });

        fab.setOnClickListener(view -> {
        });


    }
    private AlertDialog createProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        return builder.create();
    }
}
