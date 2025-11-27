package com.example.encomineda20.modelo;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "encomiendas")
public class Encomienda implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;  // Solo para Room, no usar para backend

    @NonNull
    private String cedulaCliente;

    @NonNull
    private String tipoProducto;

    private double valorDeclarado;

    @NonNull
    private String fechaSolicitud; // Ahora String para evitar problemas de Gson

    @NonNull
    private String origen;

    @NonNull
    private String destino;

    @NonNull
    private String estado;

    private boolean pagado;
    private String metodoPago;

    private String cedulaRepartidor;      // Opcional
    private String nombreDestinatario;    // Opcional
    private String telefonoDestinatario;  // Opcional
    private double precio;                // Opcional
    private double latitudDestino;        // Opcional
    private double longitudDestino;       // Opcional
    private double latitudOrigen;         // Opcional
    private double longitudOrigen;        // Opcional
    private double calificacion;          // Opcional
    private double distancia;             // Opcional

    @ColumnInfo(name = "pendiente_calificacion")
    private boolean pendienteCalificacion;

    public Encomienda(@NonNull String cedulaCliente,
                      @NonNull String tipoProducto,
                      double valorDeclarado,
                      @NonNull String fechaSolicitud,
                      @NonNull String origen,
                      @NonNull String destino,
                      @NonNull String estado) {
        this.cedulaCliente = cedulaCliente;
        this.tipoProducto = tipoProducto;
        this.valorDeclarado = valorDeclarado;
        this.fechaSolicitud = fechaSolicitud;
        this.origen = origen;
        this.destino = destino;
        this.estado = estado;
        this.pagado = false;
        this.pendienteCalificacion = false;
        this.precio = 0;
        this.latitudDestino = 0;
        this.longitudDestino = 0;
        this.latitudOrigen = 0;
        this.longitudOrigen = 0;
        this.calificacion = 0;
        this.distancia = 0;
    }

    public Encomienda() {}



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(@NonNull String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    @NonNull
    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(@NonNull String tipoProducto) { this.tipoProducto = tipoProducto; }

    public double getValorDeclarado() { return valorDeclarado; }
    public void setValorDeclarado(double valorDeclarado) { this.valorDeclarado = valorDeclarado; }

    @NonNull
    public String getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(@NonNull String fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }

    @NonNull
    public String getOrigen() { return origen; }
    public void setOrigen(@NonNull String origen) { this.origen = origen; }

    @NonNull
    public String getDestino() { return destino; }
    public void setDestino(@NonNull String destino) { this.destino = destino; }

    @NonNull
    public String getEstado() { return estado; }
    public void setEstado(@NonNull String estado) { this.estado = estado; }

    public boolean isPagado() { return pagado; }
    public void setPagado(boolean pagado) { this.pagado = pagado; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getCedulaRepartidor() { return cedulaRepartidor; }
    public void setCedulaRepartidor(String cedulaRepartidor) { this.cedulaRepartidor = cedulaRepartidor; }

    public String getNombreDestinatario() { return nombreDestinatario; }
    public void setNombreDestinatario(String nombreDestinatario) { this.nombreDestinatario = nombreDestinatario; }

    public String getTelefonoDestinatario() { return telefonoDestinatario; }
    public void setTelefonoDestinatario(String telefonoDestinatario) { this.telefonoDestinatario = telefonoDestinatario; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public double getLatitudDestino() { return latitudDestino; }
    public void setLatitudDestino(double latitudDestino) { this.latitudDestino = latitudDestino; }

    public double getLongitudDestino() { return longitudDestino; }
    public void setLongitudDestino(double longitudDestino) { this.longitudDestino = longitudDestino; }

    public double getLatitudOrigen() { return latitudOrigen; }
    public void setLatitudOrigen(double latitudOrigen) { this.latitudOrigen = latitudOrigen; }

    public double getLongitudOrigen() { return longitudOrigen; }
    public void setLongitudOrigen(double longitudOrigen) { this.longitudOrigen = longitudOrigen; }

    public double getCalificacion() { return calificacion; }
    public void setCalificacion(double calificacion) { this.calificacion = calificacion; }

    public double getDistancia() { return distancia; }
    public void setDistancia(double distancia) { this.distancia = distancia; }

    public boolean isPendienteCalificacion() { return pendienteCalificacion; }
    public void setPendienteCalificacion(boolean pendienteCalificacion) { this.pendienteCalificacion = pendienteCalificacion; }

    // --- Parcelable ---
    protected Encomienda(Parcel in) {
        id = in.readInt();
        cedulaCliente = in.readString();
        tipoProducto = in.readString();
        valorDeclarado = in.readDouble();
        fechaSolicitud = in.readString();
        origen = in.readString();
        destino = in.readString();
        estado = in.readString();
        pagado = in.readByte() != 0;
        metodoPago = in.readString();
        cedulaRepartidor = in.readString();
        nombreDestinatario = in.readString();
        telefonoDestinatario = in.readString();
        precio = in.readDouble();
        latitudDestino = in.readDouble();
        longitudDestino = in.readDouble();
        latitudOrigen = in.readDouble();
        longitudOrigen = in.readDouble();
        calificacion = in.readDouble();
        distancia = in.readDouble();
        pendienteCalificacion = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(cedulaCliente);
        dest.writeString(tipoProducto);
        dest.writeDouble(valorDeclarado);
        dest.writeString(fechaSolicitud);
        dest.writeString(origen);
        dest.writeString(destino);
        dest.writeString(estado);
        dest.writeByte((byte) (pagado ? 1 : 0));
        dest.writeString(metodoPago);
        dest.writeString(cedulaRepartidor);
        dest.writeString(nombreDestinatario);
        dest.writeString(telefonoDestinatario);
        dest.writeDouble(precio);
        dest.writeDouble(latitudDestino);
        dest.writeDouble(longitudDestino);
        dest.writeDouble(latitudOrigen);
        dest.writeDouble(longitudOrigen);
        dest.writeDouble(calificacion);
        dest.writeDouble(distancia);
        dest.writeByte((byte) (pendienteCalificacion ? 1 : 0));
    }

    @Override
    public int describeContents() { return 0; }

    public static final Parcelable.Creator<Encomienda> CREATOR = new Parcelable.Creator<Encomienda>() {
        @Override
        public Encomienda createFromParcel(Parcel in) { return new Encomienda(in); }
        @Override
        public Encomienda[] newArray(int size) { return new Encomienda[size]; }
    };

    @Override
    public String toString() {
        return "Destino: " + destino + "\n" +
                "Tipo: " + tipoProducto + " | Valor: $" + valorDeclarado + "\n" +
                "Estado: " + estado;
    }
}


