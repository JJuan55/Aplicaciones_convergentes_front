package com.example.encomineda20.validadores;


import android.text.TextUtils;

import com.example.encomineda20.modelo.Encomienda;


public class ValidacionEncomienda {

    public static String validar(Encomienda encomienda) {
        if (encomienda == null) {
            return "La encomienda no puede ser nula.";
        }
        if (TextUtils.isEmpty(encomienda.getCedulaCliente())) {
            return "Debe ingresar la cédula del cliente.";
        }
        if (TextUtils.isEmpty(encomienda.getTipoProducto())) {
            return "Debe ingresar el tipo de producto.";
        }
        if (encomienda.getValorDeclarado() < 0) {
            return "El valor declarado no puede ser  negativo.";
        }
        if (TextUtils.isEmpty(encomienda.getFechaSolicitud())) {
            return "Debe ingresar la fecha de solicitud.";
        }
        if (TextUtils.isEmpty(encomienda.getOrigen())) {
            return "Debe ingresar la dirección de origen.";
        }
        if (TextUtils.isEmpty(encomienda.getDestino())) {
            return "Debe ingresar la dirección de destino.";
        }
        if (TextUtils.isEmpty(encomienda.getEstado())) {
            return "Debe definir un estado para la encomienda.";
        }
        return null;
    }
}
