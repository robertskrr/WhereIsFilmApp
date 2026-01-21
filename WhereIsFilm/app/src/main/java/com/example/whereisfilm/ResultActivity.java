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

    /**
     * Asignamos el logo dependiendo de la plataforma
     *
     * @param nombrePlataforma
     * @return
     */
    private int obtenerLogoPlataforma(String nombrePlataforma) {
        String nombre = nombrePlataforma.toLowerCase();

        if (nombre.contains("netflix")) {
            return R.drawable.logo_netflix;
        } else if (nombre.contains("prime")) {
            return R.drawable.logo_prime;
        } else if (nombre.contains("disney")) {
            return R.drawable.logo_disney;
        } else if (nombre.contains("hbo") || nombre.contains("max")) {
            return R.drawable.logo_hbo;
        } else if (nombre.contains("apple")) {
            return R.drawable.logo_apple;
        }

        // Si no tenemos el logo, devolvemos un icono genérico
        return android.R.drawable.ic_menu_slideshow;
    }

    private void mostrarResultado() {
        txtTitulo = findViewById(R.id.textTitulo);
        container = findViewById(R.id.containerPlataformas);
        imgPortada = findViewById(R.id.imagePortada);

        String titulo = getIntent().getStringExtra("titulo");
        String urlImagen = getIntent().getStringExtra("poster");
        // Lista de plataformas
        List<StreamingOption> opciones = (List<StreamingOption>) getIntent().getSerializableExtra("listaPlataformas");

        if (titulo != null) {
            txtTitulo.setText(titulo.toUpperCase());
        }

        if (opciones != null && !opciones.isEmpty()) {
            // Tamaño del logo (40px x 40px)
            int sizePx = (int) (40 * getResources().getDisplayMetrics().density);

            for (StreamingOption opt : opciones) {
                // Llamamos al método que crea las filas
                container.addView(crearFilaPlataforma(opt, sizePx));
            }
        }

        // Método separado para la lógica de la portada
        cargarPortada(urlImagen);
    }

    /**
     * Método para generar la vista de cada plataforma
     */
    private LinearLayout crearFilaPlataforma(StreamingOption opt, int sizePx) {
        // Contenedor horizontal para logo y texto
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER);
        row.setPadding(0, 15, 0, 15);
        row.setClickable(true);
        row.setFocusable(true);

        // Centrar cada fila en el contenedor horizontal
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        rowParams.gravity = Gravity.CENTER_HORIZONTAL;
        row.setLayoutParams(rowParams);

        // Creamos ImageView del logo
        ImageView logo = new ImageView(this);
        int logoResId = obtenerLogoPlataforma(opt.plataforma.name);
        logo.setImageResource(logoResId);

        // Ajustamos el tamaño del logo
        LinearLayout.LayoutParams logoParams = new LinearLayout.LayoutParams(sizePx, sizePx);
        logoParams.setMargins(0, 0, 20, 0); // Espacio entre logo y texto
        logo.setLayoutParams(logoParams);

        // Creamos TextView por cada plataforma
        TextView tv = new TextView(this);
        tv.setText(opt.plataforma.name);
        tv.setTextSize(18);
        tv.setPadding(0, 10, 0, 10);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tv.setTextColor(colorPlataforma(opt)); // Color del enlace que varía según la plataforma

        // Añadimos el logo y el texto al contenedor
        row.addView(logo);
        row.addView(tv);

        // Acción al hacer click
        row.setOnClickListener(v -> {
            Intent webIntent = new Intent(ResultActivity.this, WebViewActivity.class);
            webIntent.putExtra("url", opt.link);
            startActivity(webIntent);
        });

        return row;
    }

    /**
     * Método para la carga de la portada de la película
     */
    private void cargarPortada(String url) {
        if (url != null && !url.isEmpty()) {
            // Asignar imagen
            Glide.with(this)
                    .load(url)
                    .centerCrop()
                    .into(imgPortada);
        } else {
            // Imagen por defecto si no tiene url
            imgPortada.setImageResource(R.drawable.no_image_available);
        }
    }

    private int colorPlataforma(StreamingOption opt) {
        String nombre = opt.plataforma.name.toLowerCase();

        if (nombre.contains("netflix")) {
            return Color.RED;
        } else if (nombre.contains("prime")) {
            return Color.BLUE;
        } else if (nombre.contains("disney")) {
            return Color.CYAN;
        } else if (nombre.contains("hbo") || nombre.contains("max")) {
            return Color.MAGENTA;
        } else if (nombre.contains("apple")) {
            return Color.WHITE;
        }

        return Color.BLACK;
    }
}