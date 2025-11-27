package com.example.encomineda20.dto.asignador;

public class AsignarEncomiendaRequest {
    private int idEncomienda;
    private String cedulaRepartidor;

    public AsignarEncomiendaRequest(int idEncomienda, String cedulaRepartidor) {
        this.idEncomienda = idEncomienda;
        this.cedulaRepartidor = cedulaRepartidor;
    }
}

