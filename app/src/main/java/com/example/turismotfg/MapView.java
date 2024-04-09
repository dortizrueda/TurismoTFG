package com.example.turismotfg;

import android.os.Bundle;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import android.widget.Toast;
import com.example.turismotfg.Entity.Places;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.ArrayList;
import java.util.List;


public class MapView extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private org.osmdroid.views.MapView mapa;
    MyLocationNewOverlay current_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.map);

        // Solicitar permisos de ubicación al inicio
        getCurrentLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // Método para obtener la ubicación actual
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Si los permisos no están otorgados, solicitarlos
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mapa = findViewById(R.id.map);
            mapa.setTileSource(TileSourceFactory.MAPNIK);
            mapa.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
            IMapController mapController = mapa.getController();
            mapController.setZoom(17.0);


            addPlaces();
            current_location = new MyLocationNewOverlay(mapa);
            current_location.enableMyLocation(); // Habilitar la visualización de la ubicación actual
            current_location.enableFollowLocation();
            mapa.getOverlays().add(current_location);

            GeoPoint position = current_location.getMyLocation();
            if (position != null) {
                mapController.setCenter(position);
            }
        }
    }

    private void addPlaces() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("places").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<Places> list_places = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String name = document.getString("name");
                    String description = document.getString("description");
                    List<String> images = (List<String>) document.get("images");
                    Double latitude = document.getDouble("latitude");
                    Double longitude = document.getDouble("longitude");
                    String audio=document.getString("audio");

                    if (name != null && description != null && images != null &&
                            latitude != null && longitude != null) {
                        Places p = new Places(name, description, images, latitude, longitude,audio);
                        list_places.add(p);
                    }
                }

                if (!list_places.isEmpty()) {
                    Toast.makeText(MapView.this, "Se han añadido " + list_places.size() + " lugares al mapa", Toast.LENGTH_SHORT).show();
                    for (Places place : list_places) {
                        Marker marker = new Marker(mapa);
                        GeoPoint point = new GeoPoint(place.getLatitud(), place.getLongitud());
                        marker.setPosition(point);
                        marker.setTitle(place.getNombre());
                        mapa.getOverlays().add(marker);
                    }
                    mapa.invalidate();
                } else {
                    Toast.makeText(MapView.this, "No se encontraron lugares para añadir al mapa", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(MapView.this, "Error al obtener lugares: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


}
