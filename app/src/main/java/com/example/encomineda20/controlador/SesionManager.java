package com.example.encomineda20.controlador;

import android.content.Context;
import android.content.SharedPreferences;

public class SesionManager {

    private static final String PREF_NAME = "sesion_cliente";
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SesionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void guardarSesion(String cedula, String nombre, String token, String rol) {
        editor.putString("cedula", cedula);
        editor.putString("nombre", nombre);
        editor.putString("token", token);
        editor.putString("rol", rol);
        editor.putBoolean("sesionActiva", true);
        editor.apply();
    }

    public String getCedulaUsuario() {
        return prefs.getString("cedula", null);
    }

    public String getNombreUsuario() {
        return prefs.getString("nombre", null);
    }

    public String getToken() {
        return prefs.getString("token", null);
    }

    public String getRol() {
        return prefs.getString("rol", null);
    }

    public boolean haySesionActiva() {
        return prefs.getBoolean("sesionActiva", false);
    }

    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }
}