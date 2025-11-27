package com.example.encomineda20.dto.cliente;

public class LoginResponse {
    private boolean success;
    private String cedula;
    private String nombre;
    private String rol;
    private String token;

    public boolean isSuccess() { return success; }
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getToken() { return token; }
    public String getRol() { return rol; }
}
