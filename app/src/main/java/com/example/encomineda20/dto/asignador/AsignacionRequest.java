package com.example.encomineda20.dto.asignador;

public class AsignacionRequest {
    private int idEncomienda;
    private String cedulaRepartidor;

    public AsignacionRequest(int idEncomienda, String cedulaRepartidor) {
        this.idEncomienda = idEncomienda;
        this.cedulaRepartidor = cedulaRepartidor;
    }
}
