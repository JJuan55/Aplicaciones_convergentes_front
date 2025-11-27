package com.example.encomineda20.vista;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SessionManagerAsignador;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.asignador.AsignadorLoginRequest;
import com.example.encomineda20.dto.asignador.AsignadorLoginResponse;
import com.example.encomineda20.modelo.Asignador;
import com.example.encomineda20.network.AsignadorApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginAsignadorActivity extends AppCompatActivity {

    private EditText etCedula, etClave;
    private Button btnLogin, btnVolver;
    private AsignadorApi api;
    private SessionManagerAsignador session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_asignador);

        etCedula = findViewById(R.id.etCedulaAsignador);
        etClave = findViewById(R.id.etClave);
        btnLogin = findViewById(R.id.btnLogin);
        btnVolver = findViewById(R.id.btnVolver);

        api = ApiClient.getInstance().create(AsignadorApi.class);
        session = new SessionManagerAsignador(this);

        btnLogin.setOnClickListener(v -> login());
        btnVolver.setOnClickListener(v -> {
            Intent intent = new Intent(LoginAsignadorActivity.this, MenuActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

    }

    private void login() {

        String cedula = etCedula.getText().toString().trim();
        String clave = etClave.getText().toString().trim();

        if (cedula.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Complete los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);
        btnLogin.setText("Ingresando...");

        AsignadorLoginRequest req = new AsignadorLoginRequest(cedula, clave);

        api.loginAsignador(req).enqueue(new Callback<AsignadorLoginResponse>() {
            @Override
            public void onResponse(Call<AsignadorLoginResponse> call, Response<AsignadorLoginResponse> response) {

                btnLogin.setEnabled(true);
                btnLogin.setText("Ingresar");

                if (response.isSuccessful() && response.body() != null) {

                    AsignadorLoginResponse data = response.body();

                    // Extraer valores seguros (evitar NPEs)
                    String cedulaGuardada = cedula; // lo que el usuario escribió
                    String nombreResp = data.getNombre() != null ? data.getNombre() : "";
                    String correoResp = data.getCorreo() != null ? data.getCorreo() : "";
                    String tokenResp = data.getToken() != null ? data.getToken() : "";

                    // LOGS para depuración inmediata
                    Log.d("LOGIN_ASIGNADOR", "RESP nombre=" + nombreResp + " correo=" + correoResp + " token=" + tokenResp);

                    // Guardar en session (usa la nueva firma)
                    session.saveLogin(
                            data.getCedula(),
                            data.getNombre(),
                            data.getCorreo(),
                            data.getToken()
                    );


                    Log.d("LOGIN_ASIGNADOR", "SESSION saved: nombre=" + session.getNombre() + " token=" + session.getToken());

                    Toast.makeText(LoginAsignadorActivity.this,
                            "Bienvenido " + (nombreResp.isEmpty() ? cedulaGuardada : nombreResp),
                            Toast.LENGTH_LONG).show();

                    startActivity(new Intent(LoginAsignadorActivity.this, MenuAsignadorActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginAsignadorActivity.this,
                            "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AsignadorLoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Ingresar");
                Toast.makeText(LoginAsignadorActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}


