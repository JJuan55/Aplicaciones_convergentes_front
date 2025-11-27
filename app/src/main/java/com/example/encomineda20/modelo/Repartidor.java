package com.example.encomineda20.modelo;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "repartidor")
public class Repartidor {
    @PrimaryKey
    @NonNull
    private String cedula;

    private String nombre;
    private String correo;
    private String clave;
    private String telefono;
    private String tipoVehiculo;
    private String modelo;
    private String matricula;

    private float calificacionPromedio;



    public Repartidor(@NonNull String cedula, String nombre, String correo, String clave,
                      String telefono, String tipoVehiculo, String modelo, String matricula) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.telefono = telefono;
        this.tipoVehiculo = tipoVehiculo;
        this.modelo = modelo;
        this.matricula = matricula;
    }

    @NonNull
    public String getCedula() { return cedula; }
    public void setCedula(@NonNull String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public float getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(float calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getTipoVehiculo() { return tipoVehiculo; }
    public void setTipoVehiculo(String tipoVehiculo) { this.tipoVehiculo = tipoVehiculo; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    @Override
    public String toString() {
        return "Repartidor{" +
                "cedula='" + cedula + '\'' +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", telefono='" + telefono + '\'' +
                ", tipoVehiculo='" + tipoVehiculo + '\'' +
                ", modelo='" + modelo + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
