package com.example.encomineda20.vista;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.EncomiendaAdapter;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.repositorios.EncomiendaBackendRepositorio;

import java.util.ArrayList;
import java.util.List;

public class VerEncomiendasActivity extends AppCompatActivity {

    private RecyclerView recyclerEncomiendas;
    private EncomiendaAdapter adapter;
    private EncomiendaBackendRepositorio repository;
    private List<Encomienda> listaEncomiendas = new ArrayList<>();
    private String cedulaUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_encomiendas);

        recyclerEncomiendas = findViewById(R.id.recyclerEncomiendas);
        recyclerEncomiendas.setLayoutManager(new LinearLayoutManager(this));

        repository = new EncomiendaBackendRepositorio();
        cedulaUsuario = getIntent().getStringExtra("cedulaUsuario");

        adapter = new EncomiendaAdapter(listaEncomiendas, encomienda -> mostrarDialogoCancelar(encomienda));
        recyclerEncomiendas.setAdapter(adapter);

        findViewById(R.id.btnVolverHome).setOnClickListener(v -> finish());

        if (cedulaUsuario != null) cargarEncomiendas();
    }

    private void cargarEncomiendas() {
        repository.obtenerPendientes(cedulaUsuario, new Callback<List<Encomienda>>() {
            @Override
            public void onResponse(Call<List<Encomienda>> call, Response<List<Encomienda>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    listaEncomiendas.clear();
                    listaEncomiendas.addAll(response.body());
                    adapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(VerEncomiendasActivity.this,
                            "Error obteniendo encomiendas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Encomienda>> call, Throwable t) {
                Toast.makeText(VerEncomiendasActivity.this,
                        "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDialogoCancelar(Encomienda encomienda) {
        new AlertDialog.Builder(this)
                .setTitle("Cancelar encomienda")
                .setMessage("¿Deseas cancelar esta encomienda?\n\nDestino: " + encomienda.getDestino())
                .setPositiveButton("Sí, cancelar", (dialog, which) -> cancelarEncomienda(encomienda))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelarEncomienda(Encomienda encomienda) {

        repository.eliminarEncomienda(encomienda.getId(), new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(VerEncomiendasActivity.this,
                        "Encomienda cancelada", Toast.LENGTH_SHORT).show();

                cargarEncomiendas();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VerEncomiendasActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}


