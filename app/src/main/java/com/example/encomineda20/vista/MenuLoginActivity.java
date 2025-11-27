package com.example.encomineda20.vista;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;

public class MenuLoginActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu_login);

            findViewById(R.id.btnLoginCliente).setOnClickListener(v ->
                    startActivity(new Intent(this, LoginClienteActivity.class)));

            findViewById(R.id.btnLoginRepartidor).setOnClickListener(v ->
                    startActivity(new Intent(this, LoginRepartidorActivity.class)));

            findViewById(R.id.btnLoginAsignador).setOnClickListener(v ->
                    startActivity(new Intent(this, LoginAsignadorActivity.class)));

            findViewById(R.id.btnVolverMenu).setOnClickListener(v -> finish());
        }
    }
