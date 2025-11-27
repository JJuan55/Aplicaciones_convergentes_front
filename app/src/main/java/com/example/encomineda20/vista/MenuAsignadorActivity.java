package com.example.encomineda20.vista;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;
import com.example.encomineda20.controlador.SessionManagerAsignador;


public class MenuAsignadorActivity extends AppCompatActivity {

    private TextView tvBienvenida, tvCorreo;
    private LinearLayout btnAsignarEncomiendas, btnCerrarSesion;
    private SessionManagerAsignador session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_asignador);

        session = new SessionManagerAsignador(this);
        session.verificarSesion(this);

        tvBienvenida = findViewById(R.id.tvBienvenidaAsignador);
        tvCorreo = findViewById(R.id.tvCorreoAsignador);
        btnAsignarEncomiendas = findViewById(R.id.btnAsignarEncomiendas);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        tvBienvenida.setText("Bienvenido, " + session.getNombre());
        tvCorreo.setText("Correo: " + session.getCorreo());

        btnAsignarEncomiendas.setOnClickListener(v -> {
            Intent intent = new Intent(MenuAsignadorActivity.this, VerEncomiendasNoAsignadasActivity.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            session.cerrarSesion();
            Intent intent = new Intent(MenuAsignadorActivity.this, MenuActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}


