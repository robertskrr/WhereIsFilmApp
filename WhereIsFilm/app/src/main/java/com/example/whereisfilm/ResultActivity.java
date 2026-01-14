package com.example.whereisfilm;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ResultActivity extends AppCompatActivity {
    private TextView txtTitulo;
    private TextView txtPlataformas;
    private ImageView imgPortada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mostrarResultado();
    }

    private void mostrarResultado() {
        txtTitulo = findViewById(R.id.textTitulo);
        txtPlataformas = findViewById(R.id.textPlataformasResult);
        imgPortada = findViewById(R.id.imagePortada);

        String titulo = getIntent().getStringExtra("titulo");
        String plataformas = getIntent().getStringExtra("plataformas");
        String urlImagen = getIntent().getStringExtra("poster");

        txtTitulo.setText(titulo);

        if (plataformas != null && !plataformas.isEmpty()) {
            txtPlataformas.setText(plataformas);
        } else {
            txtPlataformas.setText("No disponible en plataformas de España");
        }

        if (urlImagen != null && !urlImagen.isEmpty()) {
            // Asignar imagen
            Glide.with(this)
                    .load(urlImagen)
                    .centerCrop()
                    .into(imgPortada);
        } else {
            // Imagen por defecto si no tiene url
            imgPortada.setImageResource(R.drawable.no_image_available);
        }

    }
}