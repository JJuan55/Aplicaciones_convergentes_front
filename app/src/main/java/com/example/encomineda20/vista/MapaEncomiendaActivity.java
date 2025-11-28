package com.example.encomineda20.vista;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.encomineda20.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapaEncomiendaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latOrigen, lonOrigen, latDestino, lonDestino;
    private TextView tvDistancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_encomienda);

        latOrigen = getIntent().getDoubleExtra("latOrigen", 0);
        lonOrigen = getIntent().getDoubleExtra("lonOrigen", 0);
        latDestino = getIntent().getDoubleExtra("latDestino", 0);
        lonDestino = getIntent().getDoubleExtra("lonDestino", 0);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng origen = new LatLng(latOrigen, lonOrigen);
        LatLng destino = new LatLng(latDestino, lonDestino);

        mMap.addMarker(new MarkerOptions()
                .position(origen)
                .title("Recogida")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        mMap.addMarker(new MarkerOptions()
                .position(destino)
                .title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Ajustar cámara
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origen)
                .include(destino)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

        // Dibujar ruta estimada con puntos intermedios
        dibujarRutaEstimada(origen, destino);
    }

    private void dibujarRutaEstimada(LatLng origen, LatLng destino) {
        int pasos = 10;
        List<LatLng> puntos = generarPuntosIntermedios(origen, destino, pasos);

        // Línea azul
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(puntos)
                .width(8)
                .color(Color.BLUE)
                .geodesic(true);
        mMap.addPolyline(polylineOptions);

        for (int i = 1; i < puntos.size() - 1; i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(puntos.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Punto intermedio " + i));
        }

        // Distancia total
        double distanciaTotal = 0;
        for (int i = 1; i < puntos.size(); i++) {
            distanciaTotal += calcularDistanciaHaversine(puntos.get(i - 1), puntos.get(i));
        }
        Toast.makeText(this, String.format("Distancia total: %.2f metros", distanciaTotal), Toast.LENGTH_LONG).show();
    }

    private List<LatLng> generarPuntosIntermedios(LatLng origen, LatLng destino, int pasos) {
        List<LatLng> ruta = new ArrayList<>();
        for (int i = 0; i <= pasos; i++) {
            double lat = origen.latitude + (destino.latitude - origen.latitude) * (i / (double) pasos);
            double lon = origen.longitude + (destino.longitude - origen.longitude) * (i / (double) pasos);
            ruta.add(new LatLng(lat, lon));
        }
        return ruta;
    }

    private double calcularDistanciaHaversine(LatLng origen, LatLng destino) {
        final int R = 6371000;
        double lat1 = Math.toRadians(origen.latitude);
        double lon1 = Math.toRadians(origen.longitude);
        double lat2 = Math.toRadians(destino.latitude);
        double lon2 = Math.toRadians(destino.longitude);

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
