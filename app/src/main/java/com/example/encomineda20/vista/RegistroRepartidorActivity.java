package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.encomineda20.R;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.repartidor.RegistroRepartidorRequest;
import com.example.encomineda20.dto.repartidor.RepartidorLoginResponse;
import com.example.encomineda20.network.RepartidorApi;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroRepartidorActivity extends AppCompatActivity {

    private EditText etCedula, etNombre, etCorreo, etTelefono, etClave, etModelo, etMatricula;
    private RadioGroup rgTipoVehiculo;
    private Button btnRegistrar, btnVolver;
    private RepartidorApi repartidorApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_repartidor);

        etCedula = findViewById(R.id.etCedulaRepartidor);
        etNombre = findViewById(R.id.etNombreRepartidor);
        etCorreo = findViewById(R.id.etCorreoRepartidor);
        etTelefono = findViewById(R.id.etTelefonoRepartidor);
        etClave = findViewById(R.id.etClaveRepartidor);
        etModelo = findViewById(R.id.etModelo);
        etMatricula = findViewById(R.id.etMatricula);
        rgTipoVehiculo = findViewById(R.id.rgTipoVehiculo);
        btnRegistrar = findViewById(R.id.btnRegistrarRepartidor);
        btnVolver = findViewById(R.id.btnVolverMenu);

        repartidorApi = ApiClient.getInstance().create(RepartidorApi.class);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void registrar() {
        String cedula = etCedula.getText().toString().trim();
        String nombre = etNombre.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String clave = etClave.getText().toString().trim();
        String modelo = etModelo.getText().toString().trim();
        String matricula = etMatricula.getText().toString().trim();

        int sel = rgTipoVehiculo.getCheckedRadioButtonId();
        String tipoVehiculo = sel == R.id.rbCarro ? "Carro" : sel == R.id.rbMoto ? "Moto" : null;

        // VALIDACIONES FRONTEND
        if (cedula.isEmpty()) {
            etCedula.setError("La cédula es obligatoria");
            return;
        }
        if (nombre.isEmpty()) {
            etNombre.setError("El nombre es obligatorio");
            return;
        }
        if (correo.isEmpty() || !correo.contains("@")) {
            etCorreo.setError("Correo inválido");
            return;
        }
        if (telefono.isEmpty()) {
            etTelefono.setError("El teléfono es obligatorio");
            return;
        }
        if (clave.length() < 6) {
            etClave.setError("La clave debe tener mínimo 6 caracteres");
            return;
        }
        if (modelo.isEmpty()) {
            etModelo.setError("Ingrese el modelo");
            return;
        }
        if (matricula.isEmpty()) {
            etMatricula.setError("Ingrese la matrícula");
            return;
        }
        if (tipoVehiculo == null) {
            Toast.makeText(this, "Debe seleccionar un tipo de vehículo", Toast.LENGTH_SHORT).show();
            return;
        }

        RegistroRepartidorRequest req = new RegistroRepartidorRequest(
                cedula, nombre, correo, clave, telefono, tipoVehiculo, modelo, matricula
        );

        repartidorApi.registrar(req).enqueue(new Callback<RepartidorLoginResponse>() {
            @Override
            public void onResponse(Call<RepartidorLoginResponse> call, Response<RepartidorLoginResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(RegistroRepartidorActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                    // Limpiar campos
                    etCedula.setText("");
                    etNombre.setText("");
                    etCorreo.setText("");
                    etTelefono.setText("");
                    etClave.setText("");
                    etModelo.setText("");
                    etMatricula.setText("");
                    rgTipoVehiculo.clearCheck();

                    startActivity(new Intent(RegistroRepartidorActivity.this, LoginRepartidorActivity.class));
                    finish();
                }
                else {
                    try {
                        String json = response.errorBody().string();
                        JSONObject obj = new JSONObject(json);
                        String error = obj.optString("error", "Error desconocido");

                        // ERRORES QUE VIENEN DEL BACKEND
                        if (error.contains("cédula")) {
                            etCedula.setError(error);
                        }
                        else if (error.contains("matrícula")) {
                            etMatricula.setError(error);
                        }
                        else if (error.contains("correo")) {
                            etCorreo.setError(error);
                        }
                        else {
                            Toast.makeText(RegistroRepartidorActivity.this, error, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(RegistroRepartidorActivity.this, "Error inesperado", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RepartidorLoginResponse> call, Throwable t) {
                Toast.makeText(RegistroRepartidorActivity.this, "Error de servidor: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
