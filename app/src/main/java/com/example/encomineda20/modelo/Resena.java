package com.example.encomineda20.modelo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "resenas")
public class Resena {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_encomienda")
    private int idEncomienda;

    @ColumnInfo(name = "cedula_repartidor")
    private String cedulaRepartidor;

    @ColumnInfo(name = "cedula_cliente")
    private String cedulaCliente;

    @ColumnInfo(name = "calificacion")
    private float calificacion;

    @ColumnInfo(name = "comentario")
    private String comentario;

    @ColumnInfo(name = "fecha")
    private long fecha; // timestamp

    // --- Getters y Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdEncomienda() { return idEncomienda; }
    public void setIdEncomienda(int idEncomienda) { this.idEncomienda = idEncomienda; }

    public String getCedulaRepartidor() { return cedulaRepartidor; }
    public void setCedulaRepartidor(String cedulaRepartidor) { this.cedulaRepartidor = cedulaRepartidor; }

    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }

    public float getCalificacion() { return calificacion; }
    public void setCalificacion(float calificacion) { this.calificacion = calificacion; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public long getFecha() { return fecha; }
    public void setFecha(long fecha) { this.fecha = fecha; }
}

