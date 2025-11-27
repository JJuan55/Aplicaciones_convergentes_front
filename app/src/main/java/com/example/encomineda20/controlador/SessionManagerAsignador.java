package com.example.encomineda20.controlador;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.encomineda20.vista.LoginAsignadorActivity;

public class SessionManagerAsignador {

    private static final String PREF_NAME = "asignador_session_v1";
    private static final String KEY_IS_LOGGED = "is_logged";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_CEDULA = "cedula";
    private static final String KEY_CORREO = "correo";
    private static final String KEY_TOKEN = "token";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;
    private final Context context;

    public SessionManagerAsignador(Context context) {
        this.context = context.getApplicationContext();
        prefs = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Guarda la sesión mínima necesaria
    public void saveLogin(String cedula, String nombre, String correo, String token) {
        editor.putBoolean(KEY_IS_LOGGED, true);
        editor.putString(KEY_CEDULA, cedula);
        editor.putString(KEY_NOMBRE, nombre);
        editor.putString(KEY_CORREO, correo);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED, false);
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public String getNombre() {
        return prefs.getString(KEY_NOMBRE, null);
    }

    public String getCorreo() {
        return prefs.getString(KEY_CORREO, null);
    }

    public String getCedula() {
        return prefs.getString(KEY_CEDULA, null);
    }

    // Cerrar sesión (limpia todo)
    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }

    // Verifica sesión: si no hay -> redirige al login (actividad pasada)
    public void verificarSesion(Activity activity) {
        if (!isLoggedIn()) {
            Intent i = new Intent(activity, LoginAsignadorActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(i);
            activity.finish();
        }
    }
}

