package com.example.encomineda20.vista;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.encomineda20.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SeleccionarUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    private LatLng ubicacionSeleccionada;

    private TextView txtDireccionSeleccionada;
    private Button btnConfirmar;

    private AutocompleteSupportFragment autocompleteFragment;

    private static final LatLng CHAPINERO = new LatLng(4.6534, -74.0628);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ubicacion);

        txtDireccionSeleccionada = findViewById(R.id.txtDireccionSeleccionada);
        btnConfirmar = findViewById(R.id.btnConfirmarUbicacion);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        // Autocomplete Fragment
        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
        ));

        autocompleteFragment.setHint("Buscar dirección...");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng newPos = place.getLatLng();
                moverMarcador(newPos);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newPos, 16));
                txtDireccionSeleccionada.setText("📍 " + place.getAddress());
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(SeleccionarUbicacionActivity.this,
                        "Error al buscar: " + status, Toast.LENGTH_SHORT).show();
            }
        });

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) mapFragment.getMapAsync(this);

        btnConfirmar.setOnClickListener(v -> {
            if (ubicacionSeleccionada != null) {
                Intent data = new Intent();
                data.putExtra("latitud", ubicacionSeleccionada.latitude);
                data.putExtra("longitud", ubicacionSeleccionada.longitude);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Mover cámara a Chapinero
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CHAPINERO, 15));

        moverMarcador(CHAPINERO);

        mMap.setOnCameraIdleListener(() -> {
            LatLng center = mMap.getCameraPosition().target;
            ubicacionSeleccionada = center;
            obtenerDireccion(center.latitude, center.longitude);
        });

        // Tocar el mapa → mover marcador
        mMap.setOnMapClickListener(latLng -> {
            moverMarcador(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            obtenerDireccion(latLng.latitude, latLng.longitude);
        });
    }

    private void moverMarcador(LatLng punto) {
        if (marcador != null) marcador.remove();

        marcador = mMap.addMarker(
                new MarkerOptions()
                        .position(punto)
                        .title("Ubicación seleccionada")
                        .draggable(true)
        );

        ubicacionSeleccionada = punto;
    }

    private void obtenerDireccion(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(lat, lon, 1);
            if (!direcciones.isEmpty()) {
                txtDireccionSeleccionada.setText("Direccion: " + direcciones.get(0).getAddressLine(0));
            } else {
                txtDireccionSeleccionada.setText("Dirección no disponible");
            }
        } catch (Exception e) {
            txtDireccionSeleccionada.setText("Error obteniendo dirección");
        }
    }
}

