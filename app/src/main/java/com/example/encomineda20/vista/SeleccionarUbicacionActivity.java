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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SeleccionarUbicacionActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcadorSeleccionado;
    private LatLng ubicacionSeleccionada;

    private TextView txtDireccionSeleccionada;
    private Button btnConfirmar;

    private static final LatLng BOGOTA_DEFAULT = new LatLng(4.6534, -74.0628);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ubicacion);

        txtDireccionSeleccionada = findViewById(R.id.txtDireccionSeleccionada);
        btnConfirmar = findViewById(R.id.btnConfirmarUbicacion);

        // Inicializar mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Confirmar ubicación seleccionada
        btnConfirmar.setOnClickListener(v -> {
            if (ubicacionSeleccionada != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitud", ubicacionSeleccionada.latitude);
                resultIntent.putExtra("longitud", ubicacionSeleccionada.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Selecciona una ubicación en el mapa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Si hay selección previa (ubicacionSeleccionada), centrar ahí; si no, usar Bogotá
        LatLng inicio = ubicacionSeleccionada != null ? ubicacionSeleccionada : BOGOTA_DEFAULT;
        centrarMapa(inicio);

        // Permitir al usuario mover el marcador
        mMap.setOnMapClickListener(latLng -> {
            colocarMarcador(latLng);
        });
    }

    private void centrarMapa(LatLng coordenadas) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordenadas, 15));
        colocarMarcador(coordenadas);
    }

    private void colocarMarcador(LatLng latLng) {
        if (marcadorSeleccionado != null) marcadorSeleccionado.remove();
        marcadorSeleccionado = mMap.addMarker(
                new MarkerOptions().position(latLng).title("Ubicación seleccionada")
        );
        ubicacionSeleccionada = latLng;
        mostrarDireccion(latLng.latitude, latLng.longitude);
    }

    private void mostrarDireccion(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> direcciones = geocoder.getFromLocation(lat, lon, 1);
            if (direcciones != null && !direcciones.isEmpty()) {
                String direccion = direcciones.get(0).getAddressLine(0);
                txtDireccionSeleccionada.setText("📍 " + direccion);
            } else {
                txtDireccionSeleccionada.setText("No se encontró una dirección.");
            }
        } catch (IOException e) {
            txtDireccionSeleccionada.setText("Error al obtener dirección.");
        }
    }
}