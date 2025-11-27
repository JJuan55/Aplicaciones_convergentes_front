package com.example.encomineda20.modelo;

import androidx.room.Embedded;

public class ResenaConCliente {
    @Embedded
    private Resena resena;

    private String nombreCliente;

    public Resena getReseña() {
        return resena;
    }

    public void setResena(Resena resena) {
        this.resena = resena;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}
