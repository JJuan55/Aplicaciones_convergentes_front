package com.example.encomineda20.controlador;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificacionHelper {

    public static final String CHANNEL_ID = "notificaciones_encomiendas";

    public static void crearCanal(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String nombre = "Notificaciones de Encomiendas";
            String desc = "Avisos cuando tus encomiendas son entregadas";

            NotificationChannel canal = new NotificationChannel(
                    CHANNEL_ID,
                    nombre,
                    NotificationManager.IMPORTANCE_HIGH
            );
            canal.setDescription(desc);

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(canal);
        }
    }
}
