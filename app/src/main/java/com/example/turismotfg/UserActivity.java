package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Entity.Places;
import com.example.turismotfg.Entity.User;
import com.example.turismotfg.Entity.Guide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewGuide;

    private SearchView searchView;

    private FirebaseFirestore firestore;
    private CollectionReference usersReference;
    private CollectionReference guideReference;
    private ImageButton mapButton,guideButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_activity);

        fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerViewGuide = findViewById(R.id.recyclerViewGuide);
        mapButton = findViewById(R.id.gps_button);
        guideButton=findViewById(R.id.guide_button);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this, userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(userAdapter);

        guideList = new ArrayList<>();
        placeList=new ArrayList<>();
        guideAdapter = new GuideAdapter(this, guideList, placeList);
        recyclerViewGuide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGuide.setAdapter(guideAdapter);

        AlertDialog dialog = createProgressDialog();

        firestore = FirebaseFirestore.getInstance();
        usersReference = firestore.collection("users");
        guideReference = firestore.collection("guides");

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
