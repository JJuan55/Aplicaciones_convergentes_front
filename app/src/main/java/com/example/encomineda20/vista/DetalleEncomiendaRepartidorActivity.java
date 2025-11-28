package com.example.encomineda20.vista;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.encomineda20.R;
import com.example.encomineda20.controlador.DirectionsApiHelper;
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
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleEncomiendaRepartidorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView tvDetalle, tvOrigen, tvDestino, tvValor, tvEstado, tvDistancia;
    private Button btnEntregar, btnPlanearRecorrido;
    private LinearLayout layoutDetalle;
    private GoogleMap map;

    private EncomiendaDTO encomienda;
    private RepartidorApi repartidorApi;
    private String cedulaRepartidor;

    private List<LatLng> puntosGoogle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_encomienda);

        tvOrigen = findViewById(R.id.tvOrigen);
        tvDestino = findViewById(R.id.tvDestino);
        tvValor = findViewById(R.id.tvValor);
        tvEstado = findViewById(R.id.tvEstado);
        tvDistancia = findViewById(R.id.tvDistancia);
        btnEntregar = findViewById(R.id.btnEntregar);
        btnPlanearRecorrido = findViewById(R.id.btnPlanearRecorrido);
        layoutDetalle = findViewById(R.id.layoutDetalle);
        layoutDetalle.setVisibility(View.GONE);
        btnEntregar.setVisibility(View.GONE);
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

        if (mapFragment != null)
            mapFragment.getMapAsync(this);
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

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origen)
                .include(destino)
                .build();

        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
    }


    private void mostrarRecorrido() {
        if (encomienda == null || map == null) return;

        layoutDetalle.setVisibility(View.VISIBLE);
        btnEntregar.setVisibility(View.VISIBLE);

        tvOrigen.setText("Origen: " + encomienda.getOrigen());
        tvDestino.setText("Destino: " + encomienda.getDestino());
        tvValor.setText("Valor declarado: " + encomienda.getValorDeclarado());
        tvEstado.setText("Estado: " + encomienda.getEstado());

        obtenerRutaGoogle();
    }


    private void obtenerRutaGoogle() {
        LatLng origen = new LatLng(encomienda.getLatitudOrigen(), encomienda.getLongitudOrigen());
        LatLng destino = new LatLng(encomienda.getLatitudDestino(), encomienda.getLongitudDestino());

        String apiKey = getString(R.string.google_maps_key);

        DirectionsApiHelper.obtenerRuta(this, origen, destino, apiKey, new DirectionsApiHelper.RutaCallback() {
            @Override
            public void onRutaCalculada(List<LatLng> puntos, double distanciaMetros) {

                puntosGoogle = puntos;
                tvDistancia.setText("Distancia: " + String.format("%.0f", distanciaMetros) + " m");

                dibujarRutaConWaypoints(puntosGoogle);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(DetalleEncomiendaRepartidorActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void dibujarRutaConWaypoints(List<LatLng> puntos) {
        if (puntos == null || puntos.size() < 2) return;

        map.clear();

        // Origen
        map.addMarker(new MarkerOptions().position(puntos.get(0))
                .title("Origen")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        //Destino
        map.addMarker(new MarkerOptions().position(puntos.get(puntos.size() - 1))
                .title("Destino")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        map.addPolyline(new PolylineOptions()
                .addAll(puntos)
                .width(10)
                .color(Color.BLUE));

        for (int i = 1; i < puntos.size() - 1; i += Math.max(1, puntos.size() / 12)) {
            map.addMarker(new MarkerOptions()
                    .position(puntos.get(i))
                    .title("Punto " + i)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng p : puntos) builder.include(p);

        map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }


    private void entregarEncomienda() {
        EntregaEncomiendaRequest request = new EntregaEncomiendaRequest(encomienda.getId(), cedulaRepartidor);

        repartidorApi.entregarEncomienda(request).enqueue(new Callback<MensajeResponse>() {
            @Override
            public void onResponse(Call<MensajeResponse> call, Response<MensajeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                            response.body().getMensaje(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("idEncomiendaEntregada", encomienda.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<MensajeResponse> call, Throwable t) {
                Toast.makeText(DetalleEncomiendaRepartidorActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}




