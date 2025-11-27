package com.example.encomineda20.vista;



import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.dto.cliente.ClienteDTO;
import com.example.encomineda20.repositorios.ClienteRepository;
import com.example.encomineda20.validadores.ClienteValidador;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroClienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_cliente);

        TextInputEditText inputCedula = findViewById(R.id.inputCedula);
        TextInputEditText inputNombre = findViewById(R.id.inputNombre);
        TextInputEditText inputEmail = findViewById(R.id.inputEmail);
        TextInputEditText inputTelefono = findViewById(R.id.inputTelefono);
        TextInputEditText inputClave = findViewById(R.id.inputClave);
        Button btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> {
            String cedula = inputCedula.getText().toString().trim();
            String nombre = inputNombre.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String telefono = inputTelefono.getText().toString().trim();
            String clave = inputClave.getText().toString().trim();

            // Validaciones
            if (!ClienteValidador.validarCedula(cedula)) {
                inputCedula.setError("Cédula inválida");
                return;
            }
            if (!ClienteValidador.validarNombre(nombre)) {
                inputNombre.setError("Nombre muy corto");
                return;
            }
            if (!ClienteValidador.validarEmail(email)) {
                inputEmail.setError("Correo inválido");
                return;
            }
            if (!ClienteValidador.validarTelefono(telefono)) {
                inputTelefono.setError("Teléfono inválido");
                return;
            }
            if (!ClienteValidador.validarClave(clave)) {
                inputClave.setError("La clave debe tener al menos 6 caracteres");
                return;
            }

            ClienteDTO clienteDTO = new ClienteDTO(cedula, nombre, email, telefono, clave);

            ClienteRepository.getApi().registrarCliente(clienteDTO).enqueue(new Callback<Map<String, Object>>() {
                @Override
                public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                    if (response.isSuccessful() && response.body() != null) {

                        Boolean success = (Boolean) response.body().get("success");

                        // 1. REGISTRO EXITOSO
                        if (success != null && success) {
                            Toast.makeText(RegistroClienteActivity.this, "Registro exitoso", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegistroClienteActivity.this, LoginClienteActivity.class));
                            finish();

                            // Limpiar campos
                            inputCedula.setText("");
                            inputNombre.setText("");
                            inputEmail.setText("");
                            inputTelefono.setText("");
                            inputClave.setText("");

                            return;
                        }

                        // 2. MANEJO DE ERRORES DEL BACKEND
                        String error = response.body().get("error") != null
                                ? response.body().get("error").toString()
                                : "Error desconocido";

                        if (error.equals("cedula_duplicada")) {
                            inputCedula.setError("Esta cédula ya está registrada");
                            inputCedula.requestFocus();
                            return;
                        }

                        // 3. Cualquier otro error
                        Toast.makeText(RegistroClienteActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                }


                @Override
                public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                    Toast.makeText(RegistroClienteActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        Button btnVolverMenu = findViewById(R.id.btnVolverMenu);
        btnVolverMenu.setOnClickListener(v -> {
            startActivity(new Intent(RegistroClienteActivity.this, MenuActivity.class));
            finish();
        });
    }
}

