package com.example.encomineda20.dto.cliente;

public class PagoRequest {
    private boolean pagado;
    private String metodoPago;

    public PagoRequest(boolean pagado, String metodoPago) {
        this.pagado = pagado;
        this.metodoPago = metodoPago;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
// getters y setters
}
