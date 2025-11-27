package com.example.encomineda20.vista;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.HistorialUsuarioAdapter;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.repositorios.EncomiendaBackendRepositorio;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialEncomiendasUsuarioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EncomiendaBackendRepositorio repository;
    private HistorialUsuarioAdapter adapter;
    private List<Encomienda> listaEncomiendas = new ArrayList<>();
    private String cedulaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_encomiendas_usuario);

        recyclerView = findViewById(R.id.recyclerHistorialuser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = new EncomiendaBackendRepositorio();
        cedulaUsuario = getIntent().getStringExtra("cedulaUsuario");

        adapter = new HistorialUsuarioAdapter(listaEncomiendas);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnVolverHomeHistorial).setOnClickListener(v -> finish());

        if (cedulaUsuario != null) cargarHistorial();

    }

    private void cargarHistorial() {
        repository.obtenerHistorial(cedulaUsuario, new Callback<List<Encomienda>>() {
            @Override
            public void onResponse(Call<List<Encomienda>> call, Response<List<Encomienda>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaEncomiendas.clear();
                    listaEncomiendas.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (listaEncomiendas.isEmpty()) {
                        Toast.makeText(HistorialEncomiendasUsuarioActivity.this,
                                "No tienes encomiendas entregadas.",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HistorialEncomiendasUsuarioActivity.this,
                            "Error al cargar historial.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Encomienda>> call, Throwable t) {
                Toast.makeText(HistorialEncomiendasUsuarioActivity.this,
                        "No se pudo conectar con el servidor.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}

