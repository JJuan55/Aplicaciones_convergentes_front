package com.example.encomineda20.vista;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.EncomiendaAdapterAsignador;
import com.example.encomineda20.controlador.SessionManagerAsignador;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.network.AsignadorApi;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerEncomiendasNoAsignadasActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ProgressBar progress;
    private TextView tvEmpty;
    private EncomiendaAdapterAsignador adapter;
    private List<EncomiendaDTO> lista = new ArrayList<>();
    private AsignadorApi api;
    private SessionManagerAsignador session;

    private ActivityResultLauncher<Intent> seleccionarRepartidorLauncher;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_encomiendas_no_asignadas);

        session = new SessionManagerAsignador(this);
        api = ApiClient.getInstance().create(AsignadorApi.class);

        recycler = findViewById(R.id.recyclerEncomiendasNoAsignadas);
        progress = findViewById(R.id.progressLoadingEncomiendas);
        tvEmpty = findViewById(R.id.tvEmptyEncomiendas);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EncomiendaAdapterAsignador(lista, encomienda -> abrirSeleccionarRepartidor(encomienda.getId()));
        recycler.setAdapter(adapter);

        // Launcher para recibir ID de encomienda asignada
        seleccionarRepartidorLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int idAsignada = result.getData().getIntExtra("idEncomiendaAsignada", -1);
                        if (idAsignada != -1) {
                            // Buscar y remover la encomienda de la lista
                            for (int i = 0; i < lista.size(); i++) {
                                if (lista.get(i).getId() == idAsignada) {
                                    lista.remove(i);
                                    adapter.notifyItemRemoved(i);
                                    break;
                                }
                            }
                            if (lista.isEmpty()) {
                                tvEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
        );

        Button btnVolverHome = findViewById(R.id.btnVolverHome);
        btnVolverHome.setOnClickListener(v -> {
            Intent intent = new Intent(VerEncomiendasNoAsignadasActivity.this, MenuAsignadorActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        cargarEncomiendas();
    }
    private void cargarEncomiendas() {
        progress.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        String token = session.getToken();
        if (token == null) {
            Log.e("ASIGNADOR", "No hay token de sesión. Redirigiendo a login.");
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginAsignadorActivity.class));
            finish();
            return;
        }
        api.obtenerNoAsignadas().enqueue(new Callback<List<EncomiendaDTO>>() {
            @Override
            public void onResponse(Call<List<EncomiendaDTO>> call, Response<List<EncomiendaDTO>> response) {
                progress.setVisibility(View.GONE);
                Log.d("ASIGNADOR", "Llamando API: obtener encomiendas no asignadas");
                Log.d("ASIGNADOR", "RAW JSON: " + new Gson().toJson(response.body()));
                if (!response.isSuccessful()) {
                    Log.e("ASIGNADOR", "Error HTTP: " + response.code());
                    Toast.makeText(VerEncomiendasNoAsignadasActivity.this, "Error cargando encomiendas", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    List<EncomiendaDTO> data = response.body();
                    Log.d("ASIGNADOR", "Encomiendas recibidas: " + data.size());
                    lista.clear();
                    if (data.isEmpty()) {
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        tvEmpty.setVisibility(View.GONE);
                        for (EncomiendaDTO dto : data) {
                            lista.add(convertir(dto));
                        }
                    }
                } catch (Exception e) {
                    Log.e("ASIGNADOR", "ERROR PARSEANDO: " + e.getMessage(), e);
                }
                adapter.setLista(lista);
            }

            @Override
            public void onFailure(Call<List<EncomiendaDTO>> call, Throwable t) {
                progress.setVisibility(View.GONE);
                Log.e("ASIGNADOR", "FALLO API: " + t.getMessage(), t);
                Toast.makeText(VerEncomiendasNoAsignadasActivity.this, "No se pudo conectar al servidor", Toast.LENGTH_SHORT).show();
            }
        });
        recycler.addOnItemTouchListener(new RecyclerItemClickListener(this, recycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                EncomiendaDTO e = lista.get(position);
                abrirSeleccionarRepartidor(e.getId());
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private EncomiendaDTO convertir(EncomiendaDTO dto) {
        EncomiendaDTO e = new EncomiendaDTO();
        e.setId(dto.getId());
        e.setTipoProducto(dto.getDescripcion() != null ? dto.getDescripcion() : dto.getTipoProducto());
        e.setOrigen(dto.getOrigen());
        e.setDestino(dto.getDestino());
        e.setEstado(dto.getEstado() != null ? dto.getEstado() : "Pendiente");
        e.setValorDeclarado(dto.getValorDeclarado());
        e.setPrecio(dto.getPrecio());
        e.setDistancia(dto.getDistancia());
        e.setLatitudOrigen(dto.getLatitudOrigen());
        e.setLongitudOrigen(dto.getLongitudOrigen());
        e.setLatitudDestino(dto.getLatitudDestino());
        e.setLongitudDestino(dto.getLongitudDestino());
        e.setNombreDestinatario(dto.getNombreDestinatario());
        e.setTelefonoDestinatario(dto.getTelefonoDestinatario());
        return e;
    }

    private void abrirSeleccionarRepartidor(int idEncomienda) {
        Intent intent = new Intent(this, SeleccionarRepartidorActivity.class);
        intent.putExtra("encomienda_id", idEncomienda);
        seleccionarRepartidorLauncher.launch(intent);
    }


}
