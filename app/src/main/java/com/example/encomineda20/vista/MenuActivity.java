package com.example.encomineda20.vista;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.encomineda20.R;


public class MenuActivity extends AppCompatActivity {

    private Button btnIrRegistro, btnIrIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnIrRegistro = findViewById(R.id.btnIrRegistro);
        btnIrIniciarSesion = findViewById(R.id.btnIrIniciarSesion);

        btnIrRegistro.setOnClickListener(v ->
                startActivity(new Intent(this, MenuRegistroActivity.class)));

        btnIrIniciarSesion.setOnClickListener(v ->
                startActivity(new Intent(this, MenuLoginActivity.class)));
    }
}
