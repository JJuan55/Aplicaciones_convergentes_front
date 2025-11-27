package com.example.encomineda20.modelo;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "historial_repartidor")
public class HistorialRepartidor {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cedulaRepartidor;
    private String destino;
    private double valorDeclarado;
    private long fechaEntrega;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCedulaRepartidor() { return cedulaRepartidor; }
    public void setCedulaRepartidor(String cedulaRepartidor) { this.cedulaRepartidor = cedulaRepartidor; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public double getValorDeclarado() { return valorDeclarado; }
    public void setValorDeclarado(double valorDeclarado) { this.valorDeclarado = valorDeclarado; }

    public long getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(long fechaEntrega) { this.fechaEntrega = fechaEntrega; }
}
