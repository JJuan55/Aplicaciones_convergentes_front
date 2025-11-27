package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.encomineda20.dto.ApiClient;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.ItemNotificacionAdapter;
import com.example.encomineda20.controlador.SesionManager;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.network.ApiService;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class BuzonNotificacionesActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private ItemNotificacionAdapter adapter;
    private SesionManager session;
    private ApiService api;
    private List<EncomiendaDTO> lista;
    private ActivityResultLauncher<Intent> calificarLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buzon_notificaciones);

        recycler = findViewById(R.id.recyclerNotificaciones);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        lista = new ArrayList<>();

        session = new SesionManager(this);
        api = ApiClient.getInstance().create(ApiService.class);

        // Configurar launcher para refrescar lista al calificar
        calificarLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int idCalificada = result.getData().getIntExtra("idEncomiendaCalificada", -1);
                        if (idCalificada != -1) {
                            for (int i = 0; i < lista.size(); i++) {
                                if (lista.get(i).getId() == idCalificada) {
                                    lista.remove(i);
                                    adapter.notifyItemRemoved(i); // 🔹 Se elimina inmediatamente
                                    break;
                                }
                            }
                        }
                    }
                }
        );

        // Listener del adapter
        ItemNotificacionAdapter.OnItemActionListener listener = (encomienda, position) -> {
            Intent intent = new Intent(BuzonNotificacionesActivity.this, CalificacionRepartidorActivity.class);
            intent.putExtra("idEncomienda", encomienda.getId());
            intent.putExtra("cedulaCliente", session.getCedulaUsuario());
            calificarLauncher.launch(intent);
        };

        adapter = new ItemNotificacionAdapter(lista, listener);
        recycler.setAdapter(adapter);

        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        String cedula = session.getCedulaUsuario();

        api.obtenerNotificaciones(cedula).enqueue(new Callback<List<EncomiendaDTO>>() {
            @Override
            public void onResponse(Call<List<EncomiendaDTO>> call, Response<List<EncomiendaDTO>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                    Log.d("BUZON", "No hay notificaciones o error en backend");
                    lista.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }

                lista.clear();
                lista.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<EncomiendaDTO>> call, Throwable t) {
                Log.e("BUZON", "Fallo en llamada Retrofit: " + t.getMessage());
            }
        });
    }
}




