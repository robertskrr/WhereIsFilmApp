package com.example.whereisfilm;

import java.util.List;
import java.util.Map;

/**
 * @author Robert
 * Película o serie encontrada
 */
public class Show {
    public String title; // Nombre
    public ImageSet imageSet; // Portada
    // La clave es el país ("es") y el valor la lista de plataformas
    public Map<String, List<StreamingOption>> streamingOptions;
}
