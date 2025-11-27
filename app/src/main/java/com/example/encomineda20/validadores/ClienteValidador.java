package com.example.encomineda20.validadores;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.HashMap;
import java.util.Map;

public class ClienteValidador {
    public static boolean validarCedula(String cedula) {
        return cedula != null && cedula.matches("\\d{6,10}");
    }

    public static boolean validarNombre(String nombre) {
        return nombre != null && nombre.length() >= 3;
    }

    public static boolean validarEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean validarTelefono(String telefono) {
        return telefono != null && telefono.matches("\\d{7,10}");
    }

    public static boolean validarClave(String clave) {
        return clave != null && clave.length() >= 6;
    }

    /**
     * Valida los campos de inicio de sesión.
     * @param cedula Cédula ingresada por el usuario
     * @param clave Clave ingresada por el usuario
     * @return Mapa con errores. Si está vacío, no hay errores.
     */
    public Map<String, String> validarInicioSesion(String cedula, String clave) {
        Map<String, String> errores = new HashMap<>();

        if (TextUtils.isEmpty(cedula)) {
            errores.put("cedula", "La cédula es obligatoria");
        } else if (cedula.length() < 5) {
            errores.put("cedula", "La cédula debe tener al menos 5 dígitos");
        }

        if (TextUtils.isEmpty(clave)) {
            errores.put("clave", "La clave es obligatoria");
        } else if (clave.length() < 4) {
            errores.put("clave", "La clave debe tener al menos 4 caracteres");
        }

        return errores;
    }
}
