package com.example.encomineda20.dto.cliente;


public class ClienteDTO {
    private String cedula;
    private String nombre;
    private String email;
    private String telefono;
    private String clave;

    public ClienteDTO(String cedula, String nombre, String email, String telefono, String clave) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.clave = clave;
    }

    // Getters y setters
}
