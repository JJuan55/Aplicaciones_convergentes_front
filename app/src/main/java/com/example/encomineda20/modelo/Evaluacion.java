package com.example.encomineda20.modelo;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "evaluaciones")
public class Evaluacion {


    @PrimaryKey(autoGenerate = true)
    private int id;

    private int idEncomienda;
    @NonNull
    private String cedulaCliente;
    private int calificacion;
    private String comentario;
    @NonNull
    private String fecha;

    public Evaluacion(int idEncomienda, @NonNull String cedulaCliente,
                      int calificacion, String comentario, @NonNull String fecha) {
        this.idEncomienda = idEncomienda;
        this.cedulaCliente = cedulaCliente;
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.fecha = fecha;
    }


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

    @NonNull
    public String getCedulaCliente() {
        return cedulaCliente;
    }
    public void setCedulaCliente(@NonNull String cedulaCliente) {
        this.cedulaCliente = cedulaCliente;
    }

    public int getCalificacion() {
        return calificacion;
    }
    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @NonNull
    public String getFecha() {
        return fecha;
    }
    public void setFecha(@NonNull String fecha) {
        this.fecha = fecha;
    }
}
