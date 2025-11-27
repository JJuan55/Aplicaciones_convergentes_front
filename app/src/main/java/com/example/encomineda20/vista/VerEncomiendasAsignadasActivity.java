package com.example.encomineda20.vista;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.EncomiendaAdapter;

import com.example.encomineda20.controlador.EncomiendaAdapterRepartidor;
import com.example.encomineda20.controlador.SessionManagerRepartidor;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.network.RepartidorApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerEncomiendasAsignadasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EncomiendaAdapterRepartidor adapter;
    private RepartidorApi repartidorApi;
    private SessionManagerRepartidor session;
    private String cedulaRepartidor;
    private List<EncomiendaDTO> lista = new ArrayList<>();

    // Launcher para recibir encomiendas entregadas
    private ActivityResultLauncher<Intent> detalleLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_encomiendas_asignadas);

        recyclerView = findViewById(R.id.recyclerEncomiendasRepartidor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        session = new SessionManagerRepartidor(this);
        cedulaRepartidor = getIntent().getStringExtra("cedulaRepartidor");

        repartidorApi = ApiClient.getInstance().create(RepartidorApi.class);

        adapter = new EncomiendaAdapterRepartidor(lista, encomienda -> {
            Intent intent = new Intent(this, DetalleEncomiendaRepartidorActivity.class);
            intent.putExtra("idEncomienda", encomienda.getId());
            intent.putExtra("cedulaRepartidor", cedulaRepartidor);
            detalleLauncher.launch(intent);
        });

        recyclerView.setAdapter(adapter);

        // Configurar ActivityResultLauncher
        detalleLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int idEntregada = result.getData().getIntExtra("idEncomiendaEntregada", -1);
                        if (idEntregada != -1) {
                            // Remover encomienda entregada de la lista local y del adapter
                            for (int i = 0; i < lista.size(); i++) {
                                if (lista.get(i).getId() == idEntregada) {
                                    lista.remove(i);
                                    adapter.notifyItemRemoved(i);
                                    break;
                                }
                            }
                        }
                    }
                }
        );

        Button btnVolverMenu = findViewById(R.id.btnVolverMenu);
        btnVolverMenu.setOnClickListener(v -> {
            finish(); // Simple finish ya que MenuRepartidorActivity ya está en el stack
        });

        cargarEncomiendas();
    }

    private void cargarEncomiendas() {
        repartidorApi.obtenerEncomiendasAsignadas(cedulaRepartidor)
                .enqueue(new Callback<List<EncomiendaDTO>>() {
                    @Override
                    public void onResponse(Call<List<EncomiendaDTO>> call, Response<List<EncomiendaDTO>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            lista.clear();
                            // FILTRAR solo las encomiendas que NO estén entregadas
                            for (EncomiendaDTO e : response.body()) {
                                if (!"ENTREGADA".equalsIgnoreCase(e.getEstado())) {
                                    lista.add(e);
                                }
                            }
                            adapter.setLista(lista);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(VerEncomiendasAsignadasActivity.this,
                                    "No se pudieron cargar las encomiendas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EncomiendaDTO>> call, Throwable t) {
                        Toast.makeText(VerEncomiendasAsignadasActivity.this,
                                "Error de servidor: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}



