package com.example.encomineda20.dto.cliente;

public class LoginRequest {
    private String cedula;
    private String clave;

    public LoginRequest(String cedula, String clave) {
        this.cedula = cedula;
        this.clave = clave;
    }
}
