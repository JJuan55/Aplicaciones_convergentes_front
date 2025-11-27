package com.example.encomineda20.dto.repartidor;



import com.google.gson.annotations.SerializedName;

public class ResenaDTO {

    @SerializedName("id")
    private int id;

    @SerializedName("idEncomienda")
    private int idEncomienda;

    @SerializedName("cedulaCliente")
    private String cedulaCliente;

    @SerializedName("nombreCliente")
    private String nombreCliente;

    @SerializedName("calificacion")
    private Double calificacion;

    @SerializedName("comentario")
    private String comentario;

    @SerializedName("fecha")
    private String fecha;

  // ISO string, ej: "2025-11-24T15:30:00"

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEncomienda() {
        return idEncomienda;
    }

    public void setIdEncomienda(int idEncomienda) {
        this.idEncomienda = idEncomienda;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
