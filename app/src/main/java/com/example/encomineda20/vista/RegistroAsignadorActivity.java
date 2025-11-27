package com.example.encomineda20.vista;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.encomineda20.R;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.asignador.AsignadorRegistroRequest;
import com.example.encomineda20.dto.asignador.AsignadorRegistroResponse;
import com.example.encomineda20.modelo.Asignador;
import com.example.encomineda20.network.AsignadorApi;
import com.example.encomineda20.repositorios.AsignadorRepositorio;
import com.example.encomineda20.validadores.AsignadorValidador;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroAsignadorActivity extends AppCompatActivity {

    private EditText etCedula, etNombre, etCorreo, etClave;
    private Button btnRegistrar, btnVolver;
    private AsignadorApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_asignador);

        etCedula = findViewById(R.id.etCedulaAsignador);
        etNombre = findViewById(R.id.etNombreAsignador);
        etCorreo = findViewById(R.id.etCorreoAsignador);
        etClave = findViewById(R.id.etClaveAsignador);
        btnRegistrar = findViewById(R.id.btnRegistrarAsignador);
        btnVolver = findViewById(R.id.btnVolverMenuAsignador);

        api = ApiClient.getInstance().create(AsignadorApi.class);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void registrar() {

        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String clave = etClave.getText().toString().trim();

        // Validación básica
        if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegistrar.setEnabled(false);
        btnRegistrar.setText("Registrando...");

        AsignadorRegistroRequest req = new AsignadorRegistroRequest(
                cedula, nombre, correo, clave
        );

        api.registrarAsignador(req).enqueue(new Callback<AsignadorRegistroResponse>() {
            @Override
            public void onResponse(Call<AsignadorRegistroResponse> call, Response<AsignadorRegistroResponse> response) {

                btnRegistrar.setEnabled(true);
                btnRegistrar.setText("Registrar Asignador");

                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(RegistroAsignadorActivity.this,
                            "Registro exitoso",
                            Toast.LENGTH_LONG).show();

                    finish();
                } else {

                    // --------------- CAPTURA ERROR DEL BACKEND ---------------------
                    try {
                        String json = response.errorBody().string();
                        JSONObject obj = new JSONObject(json);

                        if (obj.has("error")) {
                            String error = obj.getString("error");
                            Toast.makeText(RegistroAsignadorActivity.this,
                                    error,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegistroAsignadorActivity.this,
                                    "No fue posible registrar",
                                    Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(RegistroAsignadorActivity.this,
                                "Error desconocido",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AsignadorRegistroResponse> call, Throwable t) {
                btnRegistrar.setEnabled(true);
                btnRegistrar.setText("Registrar Asignador");

                Toast.makeText(RegistroAsignadorActivity.this,
                        "Error de red o servidor",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}



