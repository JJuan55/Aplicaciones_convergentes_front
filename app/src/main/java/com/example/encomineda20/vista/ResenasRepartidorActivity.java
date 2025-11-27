package com.example.encomineda20.vista;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.ResenaAdapter;
import com.example.encomineda20.controlador.SessionManagerRepartidor;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.repartidor.ResenaDTO;
import com.example.encomineda20.modelo.ResenaConCliente;
import com.example.encomineda20.network.RepartidorApi;
import com.example.encomineda20.dto.ApiClient;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResenasRepartidorActivity extends AppCompatActivity {

    private ListView listaReseñas;
    private SessionManagerRepartidor sessionManager;
    private RepartidorApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resenas_repartidor);

        listaReseñas = findViewById(R.id.listaReseñas);
        sessionManager = new SessionManagerRepartidor(this);

        api = ApiClient.getInstance().create(RepartidorApi.class);

        cargarResenas();
    }

    private void cargarResenas() {
        String cedula = sessionManager.getCedula();
        if (cedula == null || cedula.isEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Llamada Retrofit
        api.obtenerResenas(cedula).enqueue(new Callback<List<ResenaDTO>>() {
            @Override
            public void onResponse(Call<List<ResenaDTO>> call, Response<List<ResenaDTO>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<ResenaDTO> reseñas = response.body();
                    // Crear adapter con ResenaDTO
                    ResenaAdapter adapter = new ResenaAdapter(ResenasRepartidorActivity.this, reseñas);
                    listaReseñas.setAdapter(adapter);
                } else {
                    Toast.makeText(ResenasRepartidorActivity.this, "Aún no tienes reseñas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ResenaDTO>> call, Throwable t) {
                Toast.makeText(ResenasRepartidorActivity.this, "Error al cargar reseñas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
