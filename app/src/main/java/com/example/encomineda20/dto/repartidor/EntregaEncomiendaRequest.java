package com.example.encomineda20.dto.repartidor;

public class EntregaEncomiendaRequest {
    private int idEncomienda;
    private String cedulaRepartidor;

    public EntregaEncomiendaRequest(int idEncomienda, String cedulaRepartidor) {
        this.idEncomienda = idEncomienda;
        this.cedulaRepartidor = cedulaRepartidor;
    }

    public int getIdEncomienda() { return idEncomienda; }
    public void setIdEncomienda(int idEncomienda) { this.idEncomienda = idEncomienda; }

    public String getCedulaRepartidor() { return cedulaRepartidor; }
    public void setCedulaRepartidor(String cedulaRepartidor) { this.cedulaRepartidor = cedulaRepartidor; }
}
