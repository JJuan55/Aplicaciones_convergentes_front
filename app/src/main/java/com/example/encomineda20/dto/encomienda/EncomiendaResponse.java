package com.example.encomineda20.dto.encomienda;

import com.example.encomineda20.modelo.Encomienda;

    public class EncomiendaResponse {
        private boolean success;
        private String mensaje;
        private Encomienda encomienda;

        // Constructor vacío (Gson)
        public EncomiendaResponse() {}

        public boolean isSuccess() { return success; }
        public String getMensaje() { return mensaje; }
        public Encomienda getEncomienda() { return encomienda; }

        public void setSuccess(boolean success) { this.success = success; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
        public void setEncomienda(Encomienda encomienda) { this.encomienda = encomienda; }

        @Override
        public String toString() {
            return "EncomiendaResponse{success=" + success + ", mensaje='" + mensaje + "' , encomienda=" + encomienda + "}";
        }
    }

// getters y setters


