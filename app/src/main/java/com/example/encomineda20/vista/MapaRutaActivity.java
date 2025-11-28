package com.example.encomineda20.vista;



import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.encomineda20.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaRutaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String origen, destino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ruta);

        origen = getIntent().getStringExtra("origen");
        destino = getIntent().getStringExtra("destino");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bogotaCentro = new LatLng(4.6486, -74.2479);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogotaCentro, 12));
        LatLng puntoRecogida = new LatLng(4.65, -74.08);
        LatLng puntoEntrega = new LatLng(4.63, -74.1);

        mMap.addMarker(new MarkerOptions().position(puntoRecogida).title("Punto de recogida"));
        mMap.addMarker(new MarkerOptions().position(puntoEntrega).title("Punto de entrega"));
    }
}

