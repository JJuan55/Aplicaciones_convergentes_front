package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.encomineda20.R;
import com.example.encomineda20.dto.encomienda.EncomiendaResponse;
import com.example.encomineda20.repositorios.EncomiendaBackendRepositorio;
import com.google.gson.Gson;


import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PagarEncomiendaActivity extends AppCompatActivity {

    private RadioGroup rgMetodosPago;
    private Button btnConfirmarPago;

    private String metodoPago;
    private int idEncomienda;

    private EncomiendaBackendRepositorio repo;
    private static final String TAG = "PAGAR_ENCOMIENDA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_encomienda);

        rgMetodosPago = findViewById(R.id.rgMetodosPago);
        btnConfirmarPago = findViewById(R.id.btnConfirmarPago);

        repo = new EncomiendaBackendRepositorio();

        metodoPago = "";
        idEncomienda = getIntent().getIntExtra("idEncomienda", -1);
        Log.e(TAG, "onCreate - idEncomienda=" + idEncomienda);

        btnConfirmarPago.setOnClickListener(v -> confirmarPago());
    }

    private void confirmarPago() {
        int selectedId = rgMetodosPago.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Seleccione un método de pago", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedId == R.id.rbTarjeta) metodoPago = "Tarjeta";
        else if (selectedId == R.id.rbNequi) metodoPago = "Nequi";
        else if (selectedId == R.id.rbEfectivo) metodoPago = "Efectivo";
        else metodoPago = "Otro";

        if (idEncomienda == -1) {
            Toast.makeText(this, "Error: no se encontró la encomienda a pagar", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "idEncomienda inválido: " + idEncomienda);
            return;
        }

        Log.e(TAG, "Confirmando pago id=" + idEncomienda + " metodo=" + metodoPago);

        // Usar repositorio para mantener consistencia
        repo.pagarEncomienda(idEncomienda, metodoPago, new Callback<EncomiendaResponse>() {
            @Override
            public void onResponse(Call<EncomiendaResponse> call, Response<EncomiendaResponse> response) {
                Log.e(TAG, "Pago - código: " + response.code());
                if (response.isSuccessful()) {
                    EncomiendaResponse body = response.body();
                    Log.e(TAG, "Pago - body: " + new Gson().toJson(body));
                    if (body != null && body.isSuccess()) {
                        Toast.makeText(PagarEncomiendaActivity.this, "Pago realizado con " + metodoPago, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PagarEncomiendaActivity.this, MenuUsuarioActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        String msg = (body == null) ? "Respuesta vacía" : body.getMensaje();
                        Log.e(TAG, "Pago fallido - mensaje: " + msg);
                        Toast.makeText(PagarEncomiendaActivity.this, "Error al registrar el pago: " + msg, Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        String err = response.errorBody() != null ? response.errorBody().string() : "null";
                        Log.e(TAG, "Error del servidor al pagar: " + err);
                    } catch (IOException e) {
                        Log.e(TAG, "Error leyendo errorBody", e);
                    }
                    Toast.makeText(PagarEncomiendaActivity.this, "Error al registrar el pago (código " + response.code() + ")", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EncomiendaResponse> call, Throwable t) {
                Log.e(TAG, "Fallo red en pago", t);
                Toast.makeText(PagarEncomiendaActivity.this, "No se pudo conectar con el servidor: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}


