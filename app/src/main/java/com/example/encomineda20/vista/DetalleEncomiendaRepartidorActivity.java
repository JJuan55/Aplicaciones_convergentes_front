package com.example.encomineda20.vista;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.dto.repartidor.EntregaEncomiendaRequest;
import com.example.encomineda20.dto.repartidor.MensajeResponse;
import com.example.encomineda20.network.RepartidorApi;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleEncomiendaRepartidorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvDetalle, tvOrigen, tvDestino, tvValor, tvEstado, tvDistancia;
    private Button btnVerMapa, btnEntregar, btnPlanearRecorrido;
    private LinearLayout layoutDetalle;
    private GoogleMap map;
    private EncomiendaDTO encomienda;
    private RepartidorApi repartidorApi;
    private String cedulaRepartidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_encomienda);

        tvDetalle = findViewById(R.id.tvDetalleEncomienda);
        tvOrigen = findViewById(R.id.tvOrigen);
        tvDestino = findViewById(R.id.tvDestino);
        tvValor = findViewById(R.id.tvValor);
        tvEstado = findViewById(R.id.tvEstado);
        tvDistancia = findViewById(R.id.tvDistancia);
        btnEntregar = findViewById(R.id.btnEntregar);
        btnPlanearRecorrido = findViewById(R.id.btnPlanearRecorrido);
        layoutDetalle = findViewById(R.id.layoutDetalle);

        repartidorApi = ApiClient.getInstance().create(RepartidorApi.class);
        cedulaRepartidor = getIntent().getStringExtra("cedulaRepartidor");
        int idEncomienda = getIntent().getIntExtra("idEncomienda", -1);

        cargarEncomienda(idEncomienda);

        btnPlanearRecorrido.setOnClickListener(v -> mostrarRecorrido());
        btnEntregar.setOnClickListener(v -> entregarEncomienda());
    }

    private void cargarEncomienda(int id) {
        repartidorApi.obtenerEncomiendasAsignadas(cedulaRepartidor)
                .enqueue(new Callback<List<EncomiendaDTO>>() {
                    @Override
                    public void onResponse(Call<List<EncomiendaDTO>> call, Response<List<EncomiendaDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            for (EncomiendaDTO e : response.body()) {
                                if (e.getId() == id) {
                                    encomienda = e;
                                    inicializarMapa();
                                    return;
                                }
                            }
                            Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                                    "No se encontró la encomienda solicitada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                                    "Error cargando encomienda: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EncomiendaDTO>> call, Throwable t) {
                        Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                                "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void inicializarMapa() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapaEncomienda);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        if (encomienda == null) return;

        LatLng origen = new LatLng(encomienda.getLatitudOrigen(), encomienda.getLongitudOrigen());
        LatLng destino = new LatLng(encomienda.getLatitudDestino(), encomienda.getLongitudDestino());

        map.clear();
        map.addMarker(new MarkerOptions().position(origen).title("Origen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.addMarker(new MarkerOptions().position(destino).title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(
                new LatLngBounds.Builder().include(origen).include(destino).build(), 150));
    }

    private void mostrarRecorrido() {
        if (encomienda == null || map == null) return;

        layoutDetalle.setVisibility(View.VISIBLE);
        btnEntregar.setVisibility(View.VISIBLE);

        tvOrigen.setText("Origen: " + encomienda.getOrigen());
        tvDestino.setText("Destino: " + encomienda.getDestino());
        tvValor.setText("Valor declarado: " + encomienda.getValorDeclarado());
        tvEstado.setText("Estado: " + encomienda.getEstado());

        LatLng origen = new LatLng(encomienda.getLatitudOrigen(), encomienda.getLongitudOrigen());
        LatLng destino = new LatLng(encomienda.getLatitudDestino(), encomienda.getLongitudDestino());
        List<LatLng> puntos = generarPuntosIntermedios(origen, destino, 10);

        map.clear();
        map.addMarker(new MarkerOptions().position(origen).title("Origen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        map.addMarker(new MarkerOptions().position(destino).title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        map.addPolyline(new PolylineOptions().addAll(puntos).width(8).color(Color.BLUE).geodesic(true));
        for (int i = 1; i < puntos.size() - 1; i++) {
            map.addMarker(new MarkerOptions()
                    .position(puntos.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Punto intermedio " + i));
        }

        double distanciaTotal = 0;
        for (int i = 1; i < puntos.size(); i++) {
            distanciaTotal += calcularDistanciaHaversine(puntos.get(i - 1), puntos.get(i));
        }
        tvDistancia.setText(String.format("Distancia aproximada: %.2f metros", distanciaTotal));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng p : puntos) builder.include(p);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 150));
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

    private void entregarEncomienda() {
        if (encomienda == null) return;

        EntregaEncomiendaRequest request = new EntregaEncomiendaRequest(encomienda.getId(), cedulaRepartidor);

        repartidorApi.entregarEncomienda(request).enqueue(new Callback<MensajeResponse>() {
            @Override
            public void onResponse(Call<MensajeResponse> call, Response<MensajeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                            response.body().getMensaje(), Toast.LENGTH_SHORT).show();

                    // Devolver ID entregada para actualizar lista y notificación
                    Intent intent = new Intent();
                    intent.putExtra("idEncomiendaEntregada", encomienda.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                            "Error al entregar la encomienda", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MensajeResponse> call, Throwable t) {
                Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
}




