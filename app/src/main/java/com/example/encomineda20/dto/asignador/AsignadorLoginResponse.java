package com.example.encomineda20.dto.asignador;

public class AsignadorLoginResponse {
    private String mensaje;
    private String token;
    private String rol;

    private String cedula;
    private String nombre;
    private String correo;


    public String getMensaje() {
        return mensaje;
    }

    public String getToken() {
        return token;
    }

    public String getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCedula() {
        return cedula;
    }
}

