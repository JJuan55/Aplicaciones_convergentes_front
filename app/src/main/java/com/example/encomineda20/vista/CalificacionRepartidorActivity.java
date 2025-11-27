package com.example.encomineda20.vista;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.repartidor.CalificacionRequest;
import com.example.encomineda20.dto.repartidor.MensajeResponse;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.modelo.Repartidor;
import com.example.encomineda20.modelo.Resena;
import com.example.encomineda20.network.ApiService;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalificacionRepartidorActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText txtComentario;
    private Button btnEnviar;
    private ApiService api;
    private String cedulaCliente;
    private int idEncomienda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificacion_repartidor);

        ratingBar = findViewById(R.id.ratingBar);
        txtComentario = findViewById(R.id.etComentario);
        btnEnviar = findViewById(R.id.btnEnviarCalificacion);

        api = ApiClient.getInstance().create(ApiService.class);

        cedulaCliente = getIntent().getStringExtra("cedulaCliente");
        idEncomienda = getIntent().getIntExtra("idEncomienda", -1);

        btnEnviar.setOnClickListener(v -> enviarCalificacion());
    }

    private void enviarCalificacion() {
        float calificacion = ratingBar.getRating();
        String comentario = txtComentario.getText().toString().trim();

        if (calificacion == 0) {
            Toast.makeText(this, "Por favor selecciona una calificación", Toast.LENGTH_SHORT).show();
            return;
        }

        CalificacionRequest req = new CalificacionRequest();
        req.setCalificacion(calificacion);
        req.setComentario(comentario);
        req.setCedulaCliente(cedulaCliente);

        api.calificarEncomienda(idEncomienda, req).enqueue(new Callback<MensajeResponse>() {
            @Override
            public void onResponse(Call<MensajeResponse> call, Response<MensajeResponse> response) {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(CalificacionRepartidorActivity.this,
                                "¡Gracias por tu reseña!", Toast.LENGTH_LONG).show();

                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("idEncomiendaCalificada", idEncomienda);
                        setResult(RESULT_OK, resultIntent);

                        finish();
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(CalificacionRepartidorActivity.this,
                            "Error al enviar calificación", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(Call<MensajeResponse> call, Throwable t) {
                runOnUiThread(() -> Toast.makeText(CalificacionRepartidorActivity.this,
                        "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
