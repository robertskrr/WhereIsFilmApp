package com.example.whereisfilm;

import com.google.gson.annotations.SerializedName;

/**
 * @author Robert
 * Opción de streaming
 */
public class StreamingOption implements java.io.Serializable {
    @SerializedName("service")
    public ServiceData plataforma; // Nombre de la plataforma: "netflix", "prime", etc
    public String link; // Enlace a la plataforma
}
