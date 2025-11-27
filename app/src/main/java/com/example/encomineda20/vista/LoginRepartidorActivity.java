package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SessionManagerRepartidor;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.repartidor.RepartidorLoginRequest;
import com.example.encomineda20.dto.repartidor.RepartidorLoginResponse;
import com.example.encomineda20.network.RepartidorApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginRepartidorActivity extends AppCompatActivity {

    private EditText etCedula, etClave;
    private Button btnIngresar, btnVolver;
    private RepartidorApi repartidorApi;
    private SessionManagerRepartidor session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_repartidor);

        // Inicializar UI (IDs correctos del XML)
        etCedula = findViewById(R.id.etCedula);
        etClave = findViewById(R.id.etClave);
        btnIngresar = findViewById(R.id.btnIngresar);
        btnVolver = findViewById(R.id.btnVolverMenuRepartidor);

        // Inicializar Session manager y API
        session = new SessionManagerRepartidor(this);
        repartidorApi = ApiClient.getInstance().create(RepartidorApi.class);

        btnIngresar.setOnClickListener(v -> iniciarSesion());
        btnVolver.setOnClickListener(v -> finish());
    }

    private void iniciarSesion() {
        String cedula = etCedula.getText().toString().trim();
        String clave = etClave.getText().toString().trim();

        if (cedula.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Por favor llene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RepartidorLoginRequest req = new RepartidorLoginRequest(cedula, clave);

        repartidorApi.login(req).enqueue(new Callback<RepartidorLoginResponse>() {
            @Override
            public void onResponse(Call<RepartidorLoginResponse> call, Response<RepartidorLoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RepartidorLoginResponse resp = response.body();

                    // Guardar sesión
                    session.guardarSesion(
                            resp.getCedula(),
                            resp.getNombre(),
                            resp.getToken()
                    );

                    Toast.makeText(LoginRepartidorActivity.this,
                            "Bienvenido " + resp.getNombre(), Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginRepartidorActivity.this, MenuRepartidorActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginRepartidorActivity.this,
                            "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RepartidorLoginResponse> call, Throwable t) {
                Toast.makeText(LoginRepartidorActivity.this,
                        "Error de servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}



