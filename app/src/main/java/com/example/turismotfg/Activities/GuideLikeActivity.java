package com.example.turismotfg.Activities;

import static com.example.turismotfg.MainActivity.SHARE_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.turismotfg.Adapter.GuideAdapterView;
import com.example.turismotfg.Entity.Guide;
import com.example.turismotfg.MainActivity;
import com.example.turismotfg.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
/**
 * Clase que representa la actividad que muestra todas las guías favoritas del usuario.
 * Esta actividad permite al usuario visualizar sus guías favoritas.
 * Contiene un RecyclerView para mostrar el listado de guías, un SearchView para buscar
 * dentro de la lista de guías, y un menú lateral para navegar a otras
 * funcionalidades de la aplicación
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class GuideLikeActivity extends AppCompatActivity {
    private ArrayList<Guide> guideList;
    private GuideAdapterView guideAdapter;
    private RecyclerView recyclerViewGuide;
    private SearchView searchView;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private CollectionReference guideReference;
    private ImageButton menu_icon;
    /**
     * Método que se ejecuta al iniciar la página.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_favs_activity);

        recyclerViewGuide = findViewById(R.id.recyclerViewGuide);
        searchView=findViewById(R.id.search_guide);
        searchView.clearFocus();
        menu_icon=findViewById(R.id.menu_icon);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        //Comprobación de guías favoritas
        if (currentUser != null) {
            String currentUserId = currentUser.getUid();
            Log.d("USER",currentUserId);// ID del usuario actual
            guideList = new ArrayList<>();
            guideAdapter = new GuideAdapterView(this, guideList);
            recyclerViewGuide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerViewGuide.setAdapter(guideAdapter);
            AlertDialog dialog = createProgressDialog();
            DocumentReference userRef = firestore.collection("users").document(currentUserId);
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDocument = task.getResult();
                    if (userDocument.exists()) {
                        List<String> favouriteGuideIds = (List<String>) userDocument.get("favorites"); // Obtener la lista de favoritos
                        if (favouriteGuideIds != null && !favouriteGuideIds.isEmpty()) {
                            FirebaseFirestore.getInstance()
                                    .collection("guides")
                                    .whereIn(FieldPath.documentId(), favouriteGuideIds)
                                    .addSnapshotListener((queryDocumentSnapshots, e) -> {
                                        if (e != null) {
                                            Toast.makeText(this, "Error al obtener las guías.", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        guideList.clear();
                                        if (queryDocumentSnapshots != null) {
                                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                                Guide guide = documentSnapshot.toObject(Guide.class);
                                                guideList.add(guide);
                                            }
                                            guideAdapter.notifyDataSetChanged();
                                        }
                                    });
                        } else {
                            Toast.makeText(this, "No tiene guías favoritas guardadas.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Documento del usuario no encontrado.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Error al obtener el documento del usuario.", Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            });
        } else {
            Toast.makeText(this, "Debe iniciar sesión para ver las guías favoritas.", Toast.LENGTH_LONG).show();
        }



        guideList = new ArrayList<>();
        guideAdapter = new GuideAdapterView(this, guideList);
        recyclerViewGuide.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewGuide.setAdapter(guideAdapter);
        AlertDialog dialog = createProgressDialog();

        firestore = FirebaseFirestore.getInstance();
        guideReference = firestore.collection("guides");
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
            guideAdapter.getInOrderByMedia();
            guideAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
        //Listener del SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        // Listener del Menú Lateral del sistema
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer=findViewById(R.id.container3);
                drawer.openDrawer(GravityCompat.START);
            }
        });
        NavigationView navigationView = findViewById(R.id.navigation_view);
        //Listener de los botones del interior del menú lateral
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigation_home) {
                    Intent intent = new Intent(GuideLikeActivity.this, UserActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_profile) {
                    Intent intent = new Intent(GuideLikeActivity.this, UserProfile.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_favs) {
                    Intent intent = new Intent(GuideLikeActivity.this,GuideLikeActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_help) {
                    Intent intent = new Intent(GuideLikeActivity.this,HelpActivity.class);
                    startActivity(intent);
                } else if (id == R.id.navigation_logout) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("active", false);
                    editor.apply();
                    Intent i = new Intent(GuideLikeActivity.this, MainActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });
    }
    /**
     * Filtra la guía por nombre o ciudad.
     *
     * @param newText nombre de guía o ciudad de la guía.
     */
    private void filterList(String newText) {
        List<Guide> guides=new ArrayList<>();
        for (Guide g:guideList){
            if (g.getName().toLowerCase().contains(newText.toLowerCase())||g.getCity().toLowerCase().contains(newText.toLowerCase())){
                guides.add(g);
            }
        }
        if (guides.isEmpty()){
            Toast.makeText(this,"No se han encontrado resultados",Toast.LENGTH_SHORT).show();
        }else{
            guideAdapter.setGuideListFilter(guides);
        }
    }
    /**
     * Crea un diálogo y lo devuelve
     *
     * @return El diálogo de progreso.
     */
    private AlertDialog createProgressDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuideLikeActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        return builder.create();
    }

}
