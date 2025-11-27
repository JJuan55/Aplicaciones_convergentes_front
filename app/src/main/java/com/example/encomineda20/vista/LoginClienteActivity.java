package com.example.encomineda20.vista;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SesionManager;

import com.example.encomineda20.dto.ApiClient;

import com.example.encomineda20.dto.cliente.LoginRequest;
import com.example.encomineda20.dto.cliente.LoginResponse;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.network.ApiService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginClienteActivity extends AppCompatActivity {

    private EditText etCedula, etClave;
    private TextView tvErrorCedula, tvErrorClave;
    private Button btnIngresar, btnVolverMenu;

    private ApiService apiService;
    private SesionManager sesionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cliente);

        etCedula = findViewById(R.id.etCedula);
        etClave = findViewById(R.id.etClave);
        tvErrorCedula = findViewById(R.id.tvErrorCedula);
        tvErrorClave = findViewById(R.id.tvErrorClave);
        btnIngresar = findViewById(R.id.btnIniciarSesion);
        btnVolverMenu = findViewById(R.id.btnVolverMenu);

        apiService = ApiClient.getInstance().create(ApiService.class);
        sesionManager = new SesionManager(this);

        btnIngresar.setOnClickListener(v -> intentarLogin());

        // 🔥 CORREGIDO: ahora vuelve al menú principal MenuActivity
        btnVolverMenu.setOnClickListener(v -> {
            startActivity(new Intent(LoginClienteActivity.this, MenuActivity.class));
            finish();
        });
    }

    private void intentarLogin() {
        String cedula = etCedula.getText().toString().trim();
        String clave = etClave.getText().toString().trim();

        tvErrorCedula.setVisibility(View.GONE);
        tvErrorClave.setVisibility(View.GONE);
        boolean valido = true;

        if (cedula.isEmpty()) { tvErrorCedula.setText("Ingrese su cédula"); tvErrorCedula.setVisibility(View.VISIBLE); valido = false; }
        if (clave.isEmpty()) { tvErrorClave.setText("Ingrese su clave"); tvErrorClave.setVisibility(View.VISIBLE); valido = false; }
        if (!valido) return;

        LoginRequest login = new LoginRequest(cedula, clave);

        apiService.loginCliente(login).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (!response.isSuccessful() || response.body() == null || !response.body().isSuccess()) {
                    Toast.makeText(LoginClienteActivity.this, "Cédula o clave incorrecta", Toast.LENGTH_SHORT).show();
                    Log.d("LOGIN", "Fallo login: " + response.message());
                    return;
                }

                LoginResponse data = response.body();
                sesionManager.guardarSesion(data.getCedula(), data.getNombre(), data.getToken(), data.getRol());

                Intent intent = new Intent(LoginClienteActivity.this, MenuUsuarioActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginClienteActivity.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                Log.e("LOGIN", "Error login: " + t.getMessage());
            }
        });
    }
}
