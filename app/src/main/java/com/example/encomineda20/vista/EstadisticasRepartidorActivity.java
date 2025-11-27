    package com.example.encomineda20.vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SessionManagerRepartidor;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.repartidor.EstadisticasDTO;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.network.RepartidorApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

    public class EstadisticasRepartidorActivity extends AppCompatActivity {
        private TextView txtTotalEntregas, txtPromedioCalificacion, txtDineroRecaudado;
        private Button btnVolverMenu;
        private AppDatabase db;
        private SessionManagerRepartidor sessionManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_estadisticas_repartidor);

            txtTotalEntregas = findViewById(R.id.txtTotalEntregas);
            txtPromedioCalificacion = findViewById(R.id.txtPromedioCalificacion);
            txtDineroRecaudado = findViewById(R.id.txtDineroRecaudado);
            btnVolverMenu = findViewById(R.id.btnVolverMenu);

            db = AppDatabase.getDatabase(this);
            sessionManager = new SessionManagerRepartidor(this);

            cargarEstadisticas();

            btnVolverMenu.setOnClickListener(v -> finish());
        }

        @Override
        protected void onResume() {
            super.onResume();
            cargarEstadisticas();
        }

        private void cargarEstadisticas() {
            String cedula = sessionManager.getCedula();

            ApiClient.getInstance()
                    .create(RepartidorApi.class)
                    .obtenerEstadisticas(cedula)
                    .enqueue(new Callback<EstadisticasDTO>() {
                        @Override
                        public void onResponse(Call<EstadisticasDTO> call, Response<EstadisticasDTO> response) {
                            if (!response.isSuccessful() || response.body() == null) return;

                            EstadisticasDTO est = response.body();

                            txtTotalEntregas.setText("Total de entregas: " + est.getTotalEntregas());

                            txtPromedioCalificacion.setText("Promedio de calificación: " +
                                    (est.getPromedioCalificacion() > 0 ?
                                            String.format("%.1f", est.getPromedioCalificacion()) : "N/A"));

                            txtDineroRecaudado.setText("Dinero recaudado: $" +
                                    String.format("%.2f", est.getDineroRecaudado()));
                        }

                        @Override
                        public void onFailure(Call<EstadisticasDTO> call, Throwable t) {
                            t.printStackTrace();
                        }
                    });
        }

    }

