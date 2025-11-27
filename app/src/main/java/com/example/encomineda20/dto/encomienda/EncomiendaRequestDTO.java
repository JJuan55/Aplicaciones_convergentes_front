package com.example.encomineda20.dto.encomienda;

public class EncomiendaRequestDTO {

    private String cedulaCliente;
    private String tipoProducto;
    private Double valorDeclarado;
    private String fechaSolicitud;
    private String origen;
    private String destino;
    private String metodoPago;
    private String nombreDestinatario;
    private String telefonoDestinatario;
    private String estado;
    private Boolean pagado;
    private Double precio;
    private Double distancia;
    private Double latitudOrigen;
    private Double longitudOrigen;
    private Double latitudDestino;
    private Double longitudDestino;

    public EncomiendaRequestDTO(String cedulaCliente, String tipoProducto, Double valorDeclarado,
                                String fechaSolicitud, String origen, String destino,
                                String metodoPago, String nombreDestinatario,
                                String telefonoDestinatario, Double precio, Double distancia,
                                Double latitudOrigen, Double longitudOrigen,
                                Double latitudDestino, Double longitudDestino) {

        this.cedulaCliente = cedulaCliente;
        this.tipoProducto = tipoProducto;
        this.valorDeclarado = valorDeclarado;
        this.fechaSolicitud = fechaSolicitud;
        this.origen = origen;
        this.destino = destino;
        this.metodoPago = metodoPago;
        this.nombreDestinatario = nombreDestinatario;
        this.telefonoDestinatario = telefonoDestinatario;
        this.estado = "Pendiente";
        this.pagado = false;
        this.precio = precio;
        this.distancia = distancia;
        this.latitudOrigen = latitudOrigen;
        this.longitudOrigen = longitudOrigen;
        this.latitudDestino = latitudDestino;
        this.longitudDestino = longitudDestino;
    }

    public EncomiendaRequestDTO() {
    }
    public String getCedulaCliente() {
        return cedulaCliente;
    }

    public void setCedulaCliente(String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }
    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }


    public Double getValorDeclarado() {
        return valorDeclarado;
    }

    public void setValorDeclarado(Double valorDeclarado) {
        this.valorDeclarado = valorDeclarado;
    }


    public String getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(String fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }


    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }


    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }
    public void setNombreDestinatario(String nombreDestinatario) {
        this.nombreDestinatario = nombreDestinatario;
    }

    public String getTelefonoDestinatario() {
        return telefonoDestinatario;
    }
    public void setTelefonoDestinatario(String telefonoDestinatario) {
        this.telefonoDestinatario = telefonoDestinatario;
    }

    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getPagado() {
        return pagado;
    }
    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
    }

    public Double getPrecio() {
        return precio;
    }
    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getDistancia() {
        return distancia;
    }
    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public Double getLatitudOrigen() {
        return latitudOrigen;
    }
    public void setLatitudOrigen(Double latitudOrigen) {
        this.latitudOrigen = latitudOrigen;
    }


    public Double getLongitudOrigen() {
        return longitudOrigen;
    }
    public void setLongitudOrigen(Double longitudOrigen) {
        this.longitudOrigen = longitudOrigen;
    }

    public Double getLatitudDestino() {
        return latitudDestino;
    }
    public void setLatitudDestino(Double latitudDestino) {
        this.latitudDestino = latitudDestino;
    }

    public Double getLongitudDestino() {
        return longitudDestino;
    }
    public void setLongitudDestino(Double longitudDestino) {
        this.longitudDestino = longitudDestino;
    }

    @Override
    public String toString() {
        return "EncomiendaRequestDTO{" +
                "cedulaCliente='" + cedulaCliente + '\'' +
                ", tipoProducto='" + tipoProducto + '\'' +
                ", valorDeclarado=" + valorDeclarado +
                ", fechaSolicitud='" + fechaSolicitud + '\'' +
                ", origen='" + origen + '\'' +
                ", destino='" + destino + '\'' +
                ", metodoPago='" + metodoPago + '\'' +
                ", nombreDestinatario='" + nombreDestinatario + '\'' +
                ", telefonoDestinatario='" + telefonoDestinatario + '\'' +
                ", estado='" + estado + '\'' +
                ", pagado=" + pagado +
                ", precio=" + precio +
                ", distancia=" + distancia +
                ", latitudOrigen=" + latitudOrigen +
                ", longitudOrigen=" + longitudOrigen +
                ", latitudDestino=" + latitudDestino +
                ", longitudDestino=" + longitudDestino +
                '}';
    }


}



