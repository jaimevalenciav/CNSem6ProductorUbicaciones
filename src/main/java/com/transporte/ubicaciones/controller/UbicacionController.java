package com.transporte.ubicaciones.controller;

import com.transporte.ubicaciones.model.UbicacionGPS;
import com.transporte.ubicaciones.service.UbicacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ubicacion")
public class UbicacionController {
    
    private static final Logger logger = LoggerFactory.getLogger(UbicacionController.class);
    
    private final UbicacionService ubicacionService;
    
    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> enviarUbicacion(@Valid @RequestBody UbicacionGPS ubicacion) {
        logger.info("Recibida solicitud para enviar ubicación del autobús: {}", ubicacion.getIdAutobus());
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            ubicacionService.enviarUbicacion(ubicacion);
            
            response.put("mensaje", "Ubicación enviada exitosamente a RabbitMQ");
            response.put("idAutobus", ubicacion.getIdAutobus());
            response.put("timestamp", ubicacion.getFechaHora());
            response.put("estado", "SUCCESS");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al procesar ubicación: {}", e.getMessage());
            
            response.put("mensaje", "Error al enviar ubicación");
            response.put("error", e.getMessage());
            response.put("estado", "ERROR");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "productor-ubicaciones");
        return ResponseEntity.ok(health);
    }
}
