package com.example.whereisfilm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class ResultActivity extends AppCompatActivity {
    private TextView txtTitulo;
    private ImageView imgPortada;
    private LinearLayout container; // Contenedor con las plataformas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        mostrarResultado();
    }

    private void mostrarResultado() {
        txtTitulo = findViewById(R.id.textTitulo);
        container = findViewById(R.id.containerPlataformas);
        imgPortada = findViewById(R.id.imagePortada);

        String titulo = getIntent().getStringExtra("titulo");
        String urlImagen = getIntent().getStringExtra("poster");
        // Lista de plataformas
        List<StreamingOption> opciones = (List<StreamingOption>) getIntent().getSerializableExtra("listaPlataformas");

        txtTitulo.setText(titulo.toUpperCase());

        if (opciones != null && !opciones.isEmpty()) {
            for (StreamingOption opt : opciones) {
                // Creamos TextView por cada plataforma
                TextView tv = new TextView(this);
                tv.setText(opt.plataforma.name);
                tv.setTextSize(18);
                tv.setPadding(0, 10, 0, 10);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                tv.setTextColor(Color.BLUE); // Color de enlace

                // Acción al hacer click
                tv.setOnClickListener(v -> {
                    Intent webIntent = new Intent(ResultActivity.this, WebViewActivity.class);
                    webIntent.putExtra("url", opt.link);
                    startActivity(webIntent);
                });

                container.addView(tv);
            }
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