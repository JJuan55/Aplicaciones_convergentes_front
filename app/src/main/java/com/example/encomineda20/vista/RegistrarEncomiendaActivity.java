package com.example.encomineda20.vista;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SesionManager;
import com.example.encomineda20.dto.encomienda.EncomiendaRequestDTO;
import com.example.encomineda20.dto.encomienda.EncomiendaResponse;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.repositorios.EncomiendaBackendRepositorio;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarEncomiendaActivity extends AppCompatActivity {
    private EditText etTipoProducto, etValorDeclarado, etOrigen, etDestino;
    private EditText etNombreDestinatario, etTelefonoDestinatario, etPrecio;
    private Spinner spPrioridad;
    private double latitudOrigen = 0.0, longitudOrigen = 0.0;
    private double latitudDestino = 0.0, longitudDestino = 0.0;
    private ActivityResultLauncher<Intent> mapaOrigenLauncher;
    private ActivityResultLauncher<Intent> mapaDestinoLauncher;
    private EncomiendaBackendRepositorio encomiendaRepositorio;
    private static final String TAG = "REGISTRAR_ENCOMIENDA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_encomienda);
        encomiendaRepositorio = new EncomiendaBackendRepositorio();

        // EditTexts y Spinner
        etTipoProducto = findViewById(R.id.etTipoProducto);
        etValorDeclarado = findViewById(R.id.etValorDeclarado);
        etOrigen = findViewById(R.id.etOrigen);
        etDestino = findViewById(R.id.etDestino);
        etNombreDestinatario = findViewById(R.id.etNombreDestinatario);
        etTelefonoDestinatario = findViewById(R.id.etTelefonoDestinatario);
        spPrioridad = findViewById(R.id.spPrioridad);

        // Botones ahora son LinearLayout
        LinearLayout btnGuardar = findViewById(R.id.btnRegistrar);
        LinearLayout btnSelOrigen = findViewById(R.id.btnSeleccionarOrigen);
        LinearLayout btnSelDestino = findViewById(R.id.btnSeleccionarDestino);

        // Spinner prioridad
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Normal", "Alta"}
        );
        spPrioridad.setAdapter(adapter);
        spPrioridad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) etValorDeclarado.setText("30000");
                else etValorDeclarado.setText("50000");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // Mapas
        mapaOrigenLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        latitudOrigen = result.getData().getDoubleExtra("latitud", 0);
                        longitudOrigen = result.getData().getDoubleExtra("longitud", 0);
                        etOrigen.setText(obtenerDireccion(latitudOrigen, longitudOrigen));
                    }
                });

        mapaDestinoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        latitudDestino = result.getData().getDoubleExtra("latitud", 0);
                        longitudDestino = result.getData().getDoubleExtra("longitud", 0);
                        etDestino.setText(obtenerDireccion(latitudDestino, longitudDestino));
                    }
                });

        // Listeners de los "botones" LinearLayout
        btnSelOrigen.setOnClickListener(v -> {
            Intent i = new Intent(this, SeleccionarUbicacionActivity.class);
            mapaOrigenLauncher.launch(i);
        });

        btnSelDestino.setOnClickListener(v -> {
            Intent i = new Intent(this, SeleccionarUbicacionActivity.class);
            mapaDestinoLauncher.launch(i);
        });

        btnGuardar.setOnClickListener(v -> registrarEncomienda());
    }


    private void registrarEncomienda() {
        SesionManager sesion = new SesionManager(this);
        String cedula = sesion.getCedulaUsuario();
        if (cedula == null) {
            Toast.makeText(this, "Error: no hay sesión", Toast.LENGTH_SHORT).show();
            return;
        }
        String tipo = etTipoProducto.getText().toString();
        String costo = etValorDeclarado.getText().toString();
        String origen = etOrigen.getText().toString();
        String destino = etDestino.getText().toString();
        String nombreDest = etNombreDestinatario.getText().toString();
        String telDest = etTelefonoDestinatario.getText().toString();
        if (tipo.isEmpty() || origen.isEmpty() || destino.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        double costoEncomienda = Double.parseDouble(costo);
        double propina = 5000;
        String fecha = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        EncomiendaRequestDTO dto = new EncomiendaRequestDTO();
        dto.setCedulaCliente(cedula);
        dto.setTipoProducto(tipo);
        dto.setValorDeclarado(costoEncomienda);
        dto.setPrecio(costoEncomienda + propina);
        dto.setFechaSolicitud(fecha);
        dto.setOrigen(origen);
        dto.setDestino(destino);
        dto.setLatitudOrigen(latitudOrigen);
        dto.setLongitudOrigen(longitudOrigen);
        dto.setLatitudDestino(latitudDestino);
        dto.setLongitudDestino(longitudDestino);
        dto.setNombreDestinatario(nombreDest);
        dto.setTelefonoDestinatario(telDest);
        dto.setEstado("Pendiente");
        dto.setPagado(false);
        dto.setMetodoPago("No definido");
        Log.e(TAG, "DTO a enviar: " + new Gson().toJson(dto));
        encomiendaRepositorio.crearEncomienda(dto, new Callback<EncomiendaResponse>() {
            @Override
            public void onResponse(Call<EncomiendaResponse> call, Response<EncomiendaResponse> response) {
                if (response.isSuccessful()) {
                    EncomiendaResponse body = response.body();
                    if (body != null && body.isSuccess()) {
                        Intent i = new Intent(RegistrarEncomiendaActivity.this, PagarEncomiendaActivity.class);
                        i.putExtra("idEncomienda", body.getEncomienda().getId());
                        startActivity(i);
                        finish();
                    }
                } else {
                    Toast.makeText(RegistrarEncomiendaActivity.this, "Error del servidor", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<EncomiendaResponse> call, Throwable t) {
                Toast.makeText(RegistrarEncomiendaActivity.this, "Error de red", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String obtenerDireccion(double lat, double lon) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> lista = geocoder.getFromLocation(lat, lon, 1);
            if (lista != null && !lista.isEmpty()) {
                return lista.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error obteniendo dirección", e);
        }
        return "Dirección no disponible";
    }
}
