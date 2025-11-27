package com.example.encomineda20.dto.repartidor;

public class RepartidorLoginRequest {
    private String cedula;
    private String clave;

    public RepartidorLoginRequest(String cedula, String clave) {
        this.cedula = cedula;
        this.clave = clave;
    }
}

