package com.example.encomineda20.dto.encomienda;

import com.example.encomineda20.modelo.Encomienda;

public class EncomiendaRequest {
    private Encomienda encomienda;

    public EncomiendaRequest(Encomienda encomienda) {
        this.encomienda = encomienda;
    }

    public Encomienda getEncomienda() {
        return encomienda;
    }
}
