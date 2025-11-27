package com.example.encomineda20.validadores;

import android.text.TextUtils;


public class AsignadorValidador {

    public static boolean validarCampos(String cedula, String nombre, String correo, String clave) {
        if (TextUtils.isEmpty(cedula) || TextUtils.isEmpty(nombre) ||
                TextUtils.isEmpty(correo) || TextUtils.isEmpty(clave)) {
            return false;
        }

        if (!correo.contains("@") || !correo.contains(".")) {
            return false;
        }

        return clave.length() >= 4;
    }
}


