package com.example.encomineda20.dto.encomienda;

import com.google.gson.annotations.SerializedName;

public class EncomiendaDTO {
    private int id;
    private String descripcion;           // tipoProducto o descripción
    private String tipoProducto;         // opcional
    private Double valorDeclarado;
    private String fechaSolicitud;       // formateada
    private String origen;               // direccionOrigen
    private String destino;              // direccionDestino
    private String nombreCliente;
    private String nombreDestinatario;
    private String telefonoDestinatario;
    private Double precio;
    private Double distancia;            // opcional, calculable si quieres
    private Double latitudOrigen;
    private Double longitudOrigen;
    private Double latitudDestino;
    private Double longitudDestino;
    private String estado;
    private String cedulaCliente;
    private double calificacion;

    @SerializedName("pendiente_calificacion")
//
    private Boolean pendienteCalificacion = true;

    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion;
    }

    public Boolean getPendienteCalificacion() {
        return pendienteCalificacion;
    }

    public void setPendienteCalificacion(Boolean pendienteCalificacion) {
        this.pendienteCalificacion = pendienteCalificacion;
    }

    public EncomiendaDTO(int idEncomienda) {
        this.id = idEncomienda;
    }

    public EncomiendaDTO() {
    }

    public Double getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(Double calificacion) {
        this.calificacion = calificacion;
    }

    public int getId() { return id; }
    public String getDescripcion() { return descripcion; }
    public String getTipoProducto() { return tipoProducto; }
    public Double getValorDeclarado() { return valorDeclarado; }
    public String getFechaSolicitud() { return fechaSolicitud; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getNombreCliente() { return nombreCliente; }
    public String getNombreDestinatario() { return nombreDestinatario; }
    public String getTelefonoDestinatario() { return telefonoDestinatario; }
    public Double getPrecio() { return precio; }
    public Double getDistancia() { return distancia; }
    public Double getLatitudOrigen() { return latitudOrigen; }
    public Double getLongitudOrigen() { return longitudOrigen; }
    public Double getLatitudDestino() { return latitudDestino; }
    public Double getLongitudDestino() { return longitudDestino; }

    public String getEstado() { return estado; }


    public void setEstado(String estado) { this.estado = estado; }

    public void setId(int id) {
        this.id = id;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
    public void setValorDeclarado(Double valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }
    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }
    public void setDestino(String destino) {
        this.destino = destino;
    }
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }
    public void setTelefonoDestinatario(String telefonoDestinatario) {
        this.telefonoDestinatario = telefonoDestinatario;
    }
    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public void setLatitudOrigen(Double latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }
    public void setLongitudOrigen(Double longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }
    public void setLatitudDestino(Double latitudDestino) {
        this.latitudDestino = latitudDestino;
    }
    public void setLongitudDestino(Double longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EncomiendaDTO)) return false;
        EncomiendaDTO other = (EncomiendaDTO) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}

