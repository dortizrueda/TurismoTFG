package com.example.turismotfg.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.turismotfg.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa la actividad del mapa con los marcadores de todos los lugares del sistema
 * junto a la ubicación actual del usuario.
 * Esta actividad permite al usuario visualizar el mapa con su ubicación actual y todos
 * los lugares emblemáticos del sistema.
 *
 * @autor David Ortiz Rueda
 * @version 1.0
 */
public class MapView extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private org.osmdroid.views.MapView mapa;
    MyLocationNewOverlay current_location;
    /**
     * Método que se ejecuta al iniciar la página.
     *
     * @param savedInstanceState estado de la actividad.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.map);

        // Solicitar permisos de ubicación al inicio
        getCurrentLocation();
    }
    /**
     * Solicira permisos para acceder a ubicación actual del usuario
     *
     * @param requestCode El código de solicitud de permisos.
     * @param permissions Los permisos solicitados.
     * @param grantResults Los resultados de la solicitud de permisos.
     */
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

    /**
     * Método para obtener la ubicación actual del usuario
     *
     */
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
            mapController.setZoom(15.0);


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
    /**
     * Método que añade los lugares al mapa como marcadores
     *
     */
    private void addPlaces() {
        //Obtener los lugares de la base de datos de documentos
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
                        final Places finalSelectedPlace = place;
                        //Listener para cuando se clike en un lugar visualize el perfil del lugar
                        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, org.osmdroid.views.MapView mapView) {
                                String placeName = marker.getTitle();
                                Places selectedPlace = null;
                                for (Places place : list_places) {
                                    if (place.getNombre().equals(placeName)) {
                                        selectedPlace = place;
                                        break;
                                    }
                                }
                                if (selectedPlace != null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MapView.this);
                                    builder.setMessage(selectedPlace.getNombre()+ "  ¿Desear mas información de este lugar? ")
                                            .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(MapView.this, PlaceProfile.class);
                                                    intent.putExtra("place", finalSelectedPlace);
                                                    startActivity(intent);
                                                }
                                            })
                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                                return true;
                            }
                        });
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
