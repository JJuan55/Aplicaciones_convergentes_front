package com.example.encomineda20.vista;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.NotificacionHelperRepartidor;
import com.example.encomineda20.controlador.SessionManagerRepartidor;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.network.RepartidorApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuRepartidorActivity extends AppCompatActivity {

    private LinearLayout btnVerEncomiendas, btnHistorial, btnCerrarSesion, btnEstadisticas, btnResenas;
    private TextView tvBienvenida, tvCorreo;
    private SessionManagerRepartidor sessionManager;
    private static final int PERMISO_NOTIFICACIONES = 102;

    private ActivityResultLauncher<Intent> verEncomiendasLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_repartidor);

        btnVerEncomiendas = findViewById(R.id.btnVerEncomiendas);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnEstadisticas = findViewById(R.id.btnEstadisticas);
        btnResenas = findViewById(R.id.btnResenas);

        tvBienvenida = findViewById(R.id.tvBienvenidaRepartidor);
        tvCorreo = findViewById(R.id.tvCorreoRepartidor);

        sessionManager = new SessionManagerRepartidor(this);

        if (sessionManager.getCedula() == null || sessionManager.getCedula().isEmpty()) {
            Toast.makeText(this, "Sesión expirada", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MenuActivity.class));
            finish();
            return;
        }

        tvBienvenida.setText("Bienvenido, " + sessionManager.getNombre());

        // ActivityResultLauncher para refrescar encomiendas al volver de detalle
        verEncomiendasLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        int idEntregada = result.getData().getIntExtra("idEncomiendaEntregada", -1);
                        if (idEntregada != -1) {
                            // Actualizar notificación al eliminar encomienda entregada
                            revisarEncomiendasAsignadas();
                        }
                    }
                }
        );

        btnVerEncomiendas.setOnClickListener(v -> {
            Intent intent = new Intent(this, VerEncomiendasAsignadasActivity.class);
            intent.putExtra("cedulaRepartidor", sessionManager.getCedula());
            verEncomiendasLauncher.launch(intent);
        });

        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistorialRepartidorActivity.class);
            intent.putExtra("cedulaRepartidor", sessionManager.getCedula());
            startActivity(intent);
        });

        btnEstadisticas.setOnClickListener(v -> {
            Intent intent = new Intent(this, EstadisticasRepartidorActivity.class);
            intent.putExtra("cedulaRepartidor", sessionManager.getCedula());
            startActivity(intent);
        });

        btnResenas.setOnClickListener(v -> startActivity(new Intent(this, ResenasRepartidorActivity.class)));

        btnCerrarSesion.setOnClickListener(v -> {
            sessionManager.cerrarSesion();
            Intent intent = new Intent(this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        NotificacionHelperRepartidor.crearCanal(this);
        revisarEncomiendasAsignadas();
    }

    private void revisarEncomiendasAsignadas() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISO_NOTIFICACIONES);
            return;
        }

        RepartidorApi api = ApiClient.getInstance().create(RepartidorApi.class);
        String cedula = sessionManager.getCedula();

        api.obtenerEncomiendasAsignadas(cedula).enqueue(new Callback<List<EncomiendaDTO>>() {
            @Override
            public void onResponse(Call<List<EncomiendaDTO>> call, Response<List<EncomiendaDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int cantidad = response.body().size();
                    enviarNotificacionAsignadas(cantidad);
                }
            }

            @Override
            public void onFailure(Call<List<EncomiendaDTO>> call, Throwable t) {
                Log.e("MENU_REPARTIDOR", "Error al obtener encomiendas asignadas: " + t.getMessage());
            }
        });
    }

    private void enviarNotificacionAsignadas(int cantidad) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(this);

        if (cantidad == 0) {
            manager.cancel(1);
            return;
        }

        Context ctx = this;
        Intent intent = new Intent(ctx, VerEncomiendasAsignadasActivity.class);
        intent.putExtra("cedulaRepartidor", sessionManager.getCedula());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                ctx,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String mensaje = "Tienes " + cantidad + " encomiendas asignadas.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NotificacionHelperRepartidor.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_check_box)
                .setContentTitle("Encomiendas asignadas")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.notify(1, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISO_NOTIFICACIONES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                revisarEncomiendasAsignadas();
            } else {
                Toast.makeText(this, "No se podrán mostrar notificaciones", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



