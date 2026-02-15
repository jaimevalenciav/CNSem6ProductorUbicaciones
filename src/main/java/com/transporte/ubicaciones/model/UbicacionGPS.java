package com.transporte.ubicaciones.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionGPS implements Serializable {
    
    @NotBlank(message = "El ID del autobús es requerido")
    private String idAutobus;
    
    @NotNull(message = "La latitud es requerida")
    private Double latitud;
    
    @NotNull(message = "La longitud es requerida")
    private Double longitud;
    
    @NotBlank(message = "La ruta es requerida")
    private String ruta;
    
    @NotNull(message = "La velocidad es requerida")
    private Double velocidad;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaHora;
    
    private String direccion;
    
    private String estado; // EN_RUTA, DETENIDO, FUERA_DE_SERVICIO
    
    public UbicacionGPS(String idAutobus, Double latitud, Double longitud, String ruta, Double velocidad) {
        this.idAutobus = idAutobus;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ruta = ruta;
        this.velocidad = velocidad;
        this.fechaHora = LocalDateTime.now();
        this.estado = "EN_RUTA";
    }
}
