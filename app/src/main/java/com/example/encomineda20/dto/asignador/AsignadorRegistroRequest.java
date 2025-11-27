package com.example.encomineda20.dto.asignador;
public class AsignadorRegistroRequest {
    private String cedula;
    private String nombre;
    private String correo;
    private String clave;

    public AsignadorRegistroRequest(String cedula, String nombre, String correo, String clave) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
    }
}

