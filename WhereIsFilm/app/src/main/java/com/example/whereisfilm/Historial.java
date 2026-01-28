package com.example.whereisfilm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author Robert
 * Historial de búsqqueda
 */
public class Historial extends AppCompatActivity {
    private SharedPreferences prefs;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);
        asignarComponentes();
        mostrarHistorial();
    }

    /**
     * Asigna los componentes de la interfaz
     */
    private void asignarComponentes() {
        prefs = getSharedPreferences("MisPrefs", MODE_PRIVATE);
        listView = findViewById(R.id.listViewHistorial);
    }

    /**
     * Muestra el historial
     */
    private void mostrarHistorial() {
        String historial = prefs.getString("historial", "");
        if (!historial.isEmpty()) {
            // Convertimos el String a un array separando por \n
            String[] listaBusquedas = historial.split("\n");

            // Lo pasamos con adapter para mostrarlo
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    listaBusquedas
            );
            listView.setAdapter(adapter);
        }
    }

    /**
     * Borra el historial
     *
     * @param view
     */
    public void borrarTodo(View view) {
        if (!prefs.getString("historial", "").isEmpty()) {
            // Borramos el dato de preferencias
            prefs.edit().remove("historial").apply();

            // Limpiamos la lista
            listView.setAdapter(null);

            Toast.makeText(this, R.string.historial_borrado, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.nada_que_borrar, Toast.LENGTH_SHORT).show();
        }

    }
}