package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;

public class MenuRegistroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_registro);

        findViewById(R.id.btnRegistroCliente).setOnClickListener(v ->
                startActivity(new Intent(this, RegistroClienteActivity.class)));

        findViewById(R.id.btnRegistroRepartidor).setOnClickListener(v ->
                startActivity(new Intent(this, RegistroRepartidorActivity.class)));

        findViewById(R.id.btnRegistroAsignador).setOnClickListener(v ->
                startActivity(new Intent(this, RegistroAsignadorActivity.class)));

        findViewById(R.id.btnVolverMenu).setOnClickListener(v -> finish());
    }
}

