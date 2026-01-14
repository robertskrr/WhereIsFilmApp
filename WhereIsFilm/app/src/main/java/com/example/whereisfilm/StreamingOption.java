package com.example.whereisfilm;

import com.google.gson.annotations.SerializedName;

/**
 * @author Robert
 * Opción de streaming
 */
public class StreamingOption {
    @SerializedName("service")
    public ServiceData plataforma; // Nombre de la plataforma: "netflix", "prime", etc
}
