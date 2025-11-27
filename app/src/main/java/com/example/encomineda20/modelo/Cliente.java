package com.example.encomineda20.modelo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cliente {
    @PrimaryKey(autoGenerate = true)
    private int cli_id;
    private String cli_cedula;
    private String cli_nom;
    private String cli_email;
    private String cli_tel;
    private String cli_clave;

    public Cliente(String cli_cedula, String cli_nom, String cli_email, String cli_tel, String cli_clave) {
        this.cli_cedula = cli_cedula;
        this.cli_nom = cli_nom;
        this.cli_email = cli_email;
        this.cli_tel = cli_tel;
        this.cli_clave = cli_clave;
    }

    public int getCli_id() {
        return cli_id;
    }
    public void setCli_id(int cli_id) {
        this.cli_id = cli_id;
    }

    public String getCli_cedula() {
        return cli_cedula;
    }
    public void setCli_cedula(String cli_cedula) {
        this.cli_cedula = cli_cedula;
    }

    public String getCli_nom() {
        return cli_nom;
    }
    public void setCli_nom(String cli_nom) {
        this.cli_nom = cli_nom;
    }

    public String getCli_email() {
        return cli_email;
    }
    public void setCli_email(String cli_email) {
        this.cli_email = cli_email;
    }

    public String getCli_tel() {
        return cli_tel;
    }
    public void setCli_tel(String cli_tel) {
        this.cli_tel = cli_tel;
    }

    public String getCli_clave() {
        return cli_clave;
    }
    public void setCli_clave(String cli_clave) {
        this.cli_clave = cli_clave;
    }
}
