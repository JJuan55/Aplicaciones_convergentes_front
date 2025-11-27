package com.example.encomineda20.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "asignador")
public class Asignador {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String cedula;
    private String nombre;
    private String correo;
    private String clave;

    public Asignador(String cedula, String nombre, String correo, String clave) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
}
