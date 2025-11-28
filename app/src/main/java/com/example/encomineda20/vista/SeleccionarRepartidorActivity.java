package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SessionManagerAsignador;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.asignador.AsignacionRequest;
import com.example.encomineda20.dto.repartidor.RepartidorDTO;
import com.example.encomineda20.modelo.Repartidor;
import com.example.encomineda20.network.AsignadorApi;
import com.example.encomineda20.viewmodels.EncomiendaViewModel;
import com.example.encomineda20.controlador.RepartidorAdapter;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SeleccionarRepartidorActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView tvEmpty;
    private Button btnConfirmar;
    private List<RepartidorDTO> repartidores = new ArrayList<>();
    private RepartidorAdapter adapter;
    private AsignadorApi api;
    private int idEncomienda;
    private SessionManagerAsignador session;

    private Button btnVerInfo;
    private RepartidorDTO repartidorSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_repartidor);

        recycler = findViewById(R.id.recyclerRepartidores);
        progress = findViewById(R.id.progressRepartidores);
        tvEmpty = findViewById(R.id.tvEmptyRepartidores);
        btnConfirmar = findViewById(R.id.btnConfirmarRepartidor);
        btnVerInfo = findViewById(R.id.btnVerInfoRepartidor);
        btnVerInfo.setVisibility(View.GONE);


        recycler.setLayoutManager(new LinearLayoutManager(this));

        idEncomienda = getIntent().getIntExtra("encomienda_id", -1);
        Log.d("SELEC_REPARTIDOR", "ID encomienda recibida: " + idEncomienda);

        session = new SessionManagerAsignador(this);
        api = ApiClient.getInstance().create(AsignadorApi.class);

        btnConfirmar.setEnabled(false);

        // Configurar Adapter
        adapter = new RepartidorAdapter(repartidores, repartidor -> {
            repartidorSeleccionado = repartidor;
            btnConfirmar.setEnabled(true);
            btnVerInfo.setVisibility(View.VISIBLE);
            Log.d("SELEC_REPARTIDOR", "Repartidor seleccionado: "
                    + repartidor.getNombre() + ", Cedula: " + repartidor.getCedula());
        });
        recycler.setAdapter(adapter);

        // Botón confirmar asignación
        btnConfirmar.setOnClickListener(v -> {
            if (repartidorSeleccionado != null) {
                asignarRepartidor(repartidorSeleccionado.getCedula());
            }
        });



        btnVerInfo.setOnClickListener(v -> {
            if (repartidorSeleccionado != null) {
                Intent intent = new Intent(SeleccionarRepartidorActivity.this, DetalleRepartidorActivity.class);
                intent.putExtra("cedula_repartidor", repartidorSeleccionado.getCedula());
                startActivity(intent);
            }
        });


        cargarRepartidores();
    }

    private void cargarRepartidores() {
        progress.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);

        String token = session.getToken();
        if (token == null) {
            Log.e("SELEC_REPARTIDOR", "Token nulo, sesión expirada");
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginAsignadorActivity.class));
            finish();
            return;
        }

        Log.d("SELEC_REPARTIDOR", "Llamando API para obtener repartidores disponibles...");
        api.obtenerRepartidores().enqueue(new Callback<List<RepartidorDTO>>() {
            @Override
            public void onResponse(Call<List<RepartidorDTO>> call, Response<List<RepartidorDTO>> response) {
                progress.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    repartidores.clear();
                    repartidores.addAll(response.body());
                    adapter.setLista(repartidores);

                    if (repartidores.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("SELEC_REPARTIDOR", "Error en la respuesta del backend. Código HTTP: " + response.code());
                    Toast.makeText(SeleccionarRepartidorActivity.this, "Error al cargar los repartidores", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RepartidorDTO>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.e("SELEC_REPARTIDOR", "Error al llamar API: " + t.getMessage());
                Toast.makeText(SeleccionarRepartidorActivity.this, "Error al cargar los repartidores", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void asignarRepartidor(String cedulaRepartidor) {
        Log.d("SELEC_REPARTIDOR", "Llamando API para asignar encomienda a: " + cedulaRepartidor);
        api.asignarEncomienda(new AsignacionRequest(idEncomienda, repartidorSeleccionado.getCedula()))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SeleccionarRepartidorActivity.this,
                                    "Encomienda asignada a " + repartidorSeleccionado.getNombre(),
                                    Toast.LENGTH_SHORT).show();

                            // --- Devuelve el ID de la encomienda al Activity padre ---
                            Intent data = new Intent();
                            data.putExtra("idEncomiendaAsignada", idEncomienda);
                            setResult(RESULT_OK, data);

                            finish(); // Cierra la actividad
                        } else {
                            Log.e("SELEC_REPARTIDOR", "Error al asignar encomienda, código: " + response.code());
                            Toast.makeText(SeleccionarRepartidorActivity.this, "Error al asignar encomienda", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("SELEC_REPARTIDOR", "Fallo al asignar encomienda: " + t.getMessage(), t);
                        Toast.makeText(SeleccionarRepartidorActivity.this, "Error de servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


