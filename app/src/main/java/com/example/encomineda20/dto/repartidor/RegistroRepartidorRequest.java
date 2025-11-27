package com.example.encomineda20.dto.repartidor;

public class RegistroRepartidorRequest {
    private String cedula, nombre, correo, clave, telefono, tipoVehiculo, modelo, matricula;

    public RegistroRepartidorRequest(String cedula, String nombre, String correo, String clave,
                                     String telefono, String tipoVehiculo, String modelo, String matricula) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.telefono = telefono;
        this.tipoVehiculo = tipoVehiculo;
        this.modelo = modelo;
        this.matricula = matricula;
    }
}

