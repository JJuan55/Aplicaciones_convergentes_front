package com.example.encomineda20.vista;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.repartidor.EstadisticasDTO;
import com.example.encomineda20.dto.repartidor.RepartidorDTO;
import com.example.encomineda20.network.AsignadorApi;
import com.example.encomineda20.network.RepartidorApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetalleRepartidorActivity extends AppCompatActivity {

    private TextView txtNombre, txtCedula, txtTelefono, txtTipoVehiculo, txtModeloMatricula;
    private TextView txtTotalEntregas, txtPromedioCalificacion, txtDineroRecaudado;
    private Button btnVolver;

    private String cedulaRepartidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_repartidor);

        cedulaRepartidor = getIntent().getStringExtra("cedula_repartidor");

        initViews();
        cargarInfoRepartidor();
        cargarEstadisticas();

        btnVolver.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarInfoRepartidor();
        cargarEstadisticas();
    }

    private void initViews() {
        txtNombre = findViewById(R.id.txtNombre);
        txtCedula = findViewById(R.id.txtCedula);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtTipoVehiculo = findViewById(R.id.txtTipoVehiculo);
        txtModeloMatricula = findViewById(R.id.txtModeloMatricula);

        txtTotalEntregas = findViewById(R.id.txtTotalEntregas);
        txtPromedioCalificacion = findViewById(R.id.txtPromedioCalificacion);
        txtDineroRecaudado = findViewById(R.id.txtDineroRecaudado);

        btnVolver = findViewById(R.id.btnVolver);
    }

    private void cargarInfoRepartidor() {
        ApiClient.getInstance()
                .create(RepartidorApi.class)
                .obtenerPorCedula(cedulaRepartidor)  // ⚡ nuevo método
                .enqueue(new Callback<RepartidorDTO>() {
                    @Override
                    public void onResponse(Call<RepartidorDTO> call, Response<RepartidorDTO> response) {
                        if (!response.isSuccessful() || response.body() == null) return;

                        RepartidorDTO r = response.body();

                        txtNombre.setText("Nombre: " + r.getNombre());
                        txtCedula.setText("Cédula: " + r.getCedula());
                        txtTelefono.setText("Teléfono: " + r.getTelefono());
                        txtTipoVehiculo.setText("Vehículo: " + r.getTipoVehiculo());
                        txtModeloMatricula.setText("Modelo: " + r.getModelo() + " | Matrícula: " + r.getMatricula());
                    }

                    @Override
                    public void onFailure(Call<RepartidorDTO> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

    // ================================================
    // Cargar estadísticas del repartidor
    // ================================================
    private void cargarEstadisticas() {
        ApiClient.getInstance()
                .create(RepartidorApi.class)
                .obtenerEstadisticas(cedulaRepartidor)
                .enqueue(new Callback<EstadisticasDTO>() {
                    @Override
                    public void onResponse(Call<EstadisticasDTO> call, Response<EstadisticasDTO> response) {
                        if (!response.isSuccessful() || response.body() == null) return;

                        EstadisticasDTO est = response.body();

                        txtTotalEntregas.setText("Total de entregas: " + est.getTotalEntregas());
                        txtPromedioCalificacion.setText("Promedio de calificación: " +
                                (est.getPromedioCalificacion() > 0
                                        ? String.format("%.1f", est.getPromedioCalificacion())
                                        : "N/A"));
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


