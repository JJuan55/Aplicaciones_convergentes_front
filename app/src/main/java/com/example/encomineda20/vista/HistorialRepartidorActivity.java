package com.example.encomineda20.vista;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.HistorialAdapter;

import com.example.encomineda20.controlador.HistorialRepartidorAdapter;
import com.example.encomineda20.controlador.SessionManagerRepartidor;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.modelo.HistorialRepartidor;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialRepartidorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private HistorialRepartidorAdapter adapter;
    private List<Encomienda> lista = new ArrayList<>();

    private String cedulaRepartidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_repartidor);

        recyclerView = findViewById(R.id.recyclerHistorial);
        swipeRefresh = findViewById(R.id.swipeRefresh);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HistorialRepartidorAdapter(convertirDTO(lista));
        recyclerView.setAdapter(adapter);

        cedulaRepartidor = getIntent().getStringExtra("cedulaRepartidor");

        cargarHistorial();

        swipeRefresh.setOnRefreshListener(this::cargarHistorial);
    }

    private void cargarHistorial() {
        swipeRefresh.setRefreshing(true);

        ApiClient.getEncomiendaService()
                .obtenerHistorialRepartidor(cedulaRepartidor)
                .enqueue(new Callback<List<Encomienda>>() {
                    @Override
                    public void onResponse(Call<List<Encomienda>> call, Response<List<Encomienda>> response) {
                        swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful() && response.body() != null) {
                            lista.clear();
                            lista.addAll(response.body());

                            adapter.updateList(convertirDTO(lista));
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Encomienda>> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(HistorialRepartidorActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<EncomiendaDTO> convertirDTO(List<Encomienda> origen) {
        List<EncomiendaDTO> salida = new ArrayList<>();

        for (Encomienda e : origen) {
            EncomiendaDTO dto = new EncomiendaDTO();
            dto.setDescripcion(e.getTipoProducto());
            dto.setNombreCliente(e.getCedulaCliente());
            dto.setNombreDestinatario(e.getNombreDestinatario());
            dto.setDestino(e.getDestino());
            dto.setPrecio(e.getPrecio());
            dto.setFechaSolicitud(e.getFechaSolicitud().toString());
            salida.add(dto);
        }
        return salida;
    }
}
