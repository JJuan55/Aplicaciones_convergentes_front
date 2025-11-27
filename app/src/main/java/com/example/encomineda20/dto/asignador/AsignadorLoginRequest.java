package com.example.encomineda20.dto.asignador;

public class AsignadorLoginRequest {
    private String cedula;
    private String clave;

    public AsignadorLoginRequest(String usuario, String clave) {
        this.cedula = usuario;
        this.clave = clave;
    }

    public String getCedula() {
        return this.cedula;
    }

    public String getClave() {
        return clave;
    }
}
