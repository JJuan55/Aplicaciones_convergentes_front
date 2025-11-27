package com.example.encomineda20.vista;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.NotificacionHelper;
import com.example.encomineda20.controlador.SesionManager;
import com.example.encomineda20.db.AppDatabase;
import com.example.encomineda20.dto.ApiClient;
import com.example.encomineda20.dto.encomienda.EncomiendaDTO;
import com.example.encomineda20.modelo.Encomienda;
import com.example.encomineda20.network.ApiService;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuUsuarioActivity extends AppCompatActivity {

    private TextView tvBienvenida;
    private LinearLayout btnSolicitarEncomienda, btnVerEncomiendas, btnCerrarSesion, btnHistorial, btnBuzon;
    private SesionManager sesionManager;
    private ApiService api;
    private static final int PERMISO_NOTIFICACIONES = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);


        sesionManager = new SesionManager(this);
        api = ApiClient.getInstance().create(ApiService.class);

        tvBienvenida = findViewById(R.id.tvBienvenida);
        btnSolicitarEncomienda = findViewById(R.id.btnSolicitarEncomienda);
        btnVerEncomiendas = findViewById(R.id.btnVerEncomiendas);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnHistorial = findViewById(R.id.btnHistorial);
        btnBuzon = findViewById(R.id.btnBuzon);

        if (!sesionManager.haySesionActiva()) {
            startActivity(new Intent(this, LoginClienteActivity.class));
            finish();
            return;
        }

        tvBienvenida.setText("Bienvenido, " + sesionManager.getNombreUsuario());

        btnSolicitarEncomienda.setOnClickListener(v ->
                startActivity(new Intent(MenuUsuarioActivity.this, RegistrarEncomiendaActivity.class))
        );

        btnVerEncomiendas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUsuarioActivity.this, VerEncomiendasActivity.class);
            intent.putExtra("cedulaUsuario", sesionManager.getCedulaUsuario());
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            sesionManager.cerrarSesion();
            startActivity(new Intent(MenuUsuarioActivity.this, MenuActivity.class));
            finish();
        });


        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(MenuUsuarioActivity.this, HistorialEncomiendasUsuarioActivity.class);
            intent.putExtra("cedulaUsuario", sesionManager.getCedulaUsuario());
            startActivity(intent);
        });


        btnBuzon.setOnClickListener(v ->
                startActivity(new Intent(this, BuzonNotificacionesActivity.class))
        );

        revisarEncomiendasPendientes();
        NotificacionHelper.crearCanal(this);
    }

    private void revisarEncomiendasPendientes() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    PERMISO_NOTIFICACIONES);
            return; // Espera a que el usuario conceda permiso
        }

        String cedula = sesionManager.getCedulaUsuario();
        api.obtenerNotificaciones(cedula).enqueue(new Callback<List<EncomiendaDTO>>() {
            @Override
            public void onResponse(Call<List<EncomiendaDTO>> call, Response<List<EncomiendaDTO>> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().isEmpty()) {
                    Log.d("MENU", "No hay encomiendas pendientes para notificar");
                    return;
                }

                List<EncomiendaDTO> pendientes = response.body();
                Log.d("MENU", "Encomiendas pendientes: " + pendientes.size());
                Log.d("MENU_JSON", new Gson().toJson(pendientes));

                for (EncomiendaDTO encomienda : pendientes) {
                    enviarNotificacionCalificacion(encomienda);
                }
            }

            @Override
            public void onFailure(Call<List<EncomiendaDTO>> call, Throwable t) {
                Log.e("MENU", "Error obteniendo encomiendas pendientes: " + t.getMessage());
            }
        });
    }

    /**
     * private void mostrarDialogoCalificacion(EncomiendaDTO encomienda) {
     * new AlertDialog.Builder(this)
     * .setTitle("Encomienda entregada")
     * .setMessage("Tu encomienda #" + encomienda.getId() + " ha sido entregada. ¿Deseas calificar al repartidor?")
     * .setPositiveButton("Calificar", (dialog, which) -> {
     * Intent intent = new Intent(MenuUsuarioActivity.this, CalificacionRepartidorActivity.class);
     * intent.putExtra("idEncomienda", encomienda.getId());
     * intent.putExtra("cedulaCliente", sesionManager.getCedulaUsuario());
     * startActivity(intent);
     * })
     * .setNegativeButton("Cancelar", null)
     * .setCancelable(false)
     * .show();
     * }
     **/
    private void enviarNotificacionCalificacion(EncomiendaDTO encomienda) {
        Context ctx = this;

        // Cuando el usuario toque la notificación → abrirá CalificacionRepartidorActivity
        Intent intent = new Intent(ctx, CalificacionRepartidorActivity.class);
        intent.putExtra("idEncomienda", encomienda.getId());
        intent.putExtra("cedulaCliente", sesionManager.getCedulaUsuario());

        PendingIntent pendingIntent = PendingIntent.getActivity(
                ctx,
                encomienda.getId(),  // cada notificación = ID única
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NotificacionHelper.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_check_box)
                .setContentTitle("Encomienda entregada")
                .setContentText("Tu encomienda #" + encomienda.getId() + " está lista para calificar.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);


        NotificationManagerCompat manager = NotificationManagerCompat.from(ctx);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        manager.notify(encomienda.getId(), builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISO_NOTIFICACIONES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("MENU", "Permiso de notificaciones concedido");
                // Volver a revisar encomiendas pendientes
                revisarEncomiendasPendientes();
            } else {
                Log.w("MENU", "Permiso de notificaciones denegado");
                Toast.makeText(this, "No se podrán mostrar notificaciones", Toast.LENGTH_SHORT).show();
            }
        }


    }
}


