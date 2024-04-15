package com.example.turismotfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

public class MapGuideView extends AppCompatActivity {
    private org.osmdroid.views.MapView mapa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        setContentView(R.layout.map);

        List<Double> longitudes = new ArrayList<>(),latitudes=new ArrayList<>();
        List<String> placeNames=new ArrayList<>();
        Intent i = getIntent();
        if (i != null) {
            longitudes = (List<Double>) i.getSerializableExtra("longitudes");
            latitudes = (List<Double>) i.getSerializableExtra("latitudes");
            placeNames = i.getStringArrayListExtra("names");

        }
        int size_long=longitudes.size();
        int size_lat=latitudes.size();
        mapa = findViewById(R.id.map);
        mapa.setTileSource(TileSourceFactory.MAPNIK);
        mapa.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        IMapController mapController = mapa.getController();
        GeoPoint init=new GeoPoint(37.877787, -4.766466);
        mapController.setCenter(init);
        mapController.setZoom(15.0);
        if (size_lat!=size_long){
            Toast.makeText(this,"No coinciden las longitudes, error en la apertura del mapa",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            for (int j=0;j<size_long;j++){
                Marker marker=new Marker(mapa);
                GeoPoint position=new GeoPoint(latitudes.get(j), longitudes.get(j));
                marker.setPosition(position);
                marker.setTitle(placeNames.get(j));
                mapa.getOverlays().add(marker);
            }
            mapa.invalidate();

        }

    }
}
