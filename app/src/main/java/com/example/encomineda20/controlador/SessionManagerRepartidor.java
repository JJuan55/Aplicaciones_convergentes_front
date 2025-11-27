package com.example.encomineda20.controlador;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagerRepartidor {

    private static final String PREF_NAME = "RepartidorPrefs";
    private static final String KEY_CEDULA = "key_cedula";
    private static final String KEY_NOMBRE = "key_nombre";
    private static final String KEY_TOKEN = "key_token";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManagerRepartidor(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }


    public void guardarSesion(String cedula, String nombre, String token) {
        editor.putString(KEY_CEDULA, cedula);
        editor.putString(KEY_NOMBRE, nombre);
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }

    public String getCedula() {
        return prefs.getString(KEY_CEDULA, null);
    }

    public String getNombre() {
        return prefs.getString(KEY_NOMBRE, "");
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void cerrarSesion() {
        editor.clear();
        editor.apply();
    }
}
