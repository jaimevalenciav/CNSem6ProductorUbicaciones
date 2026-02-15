package com.transporte.ubicaciones.service;

import com.transporte.ubicaciones.model.UbicacionGPS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UbicacionService {
    
    private static final Logger logger = LoggerFactory.getLogger(UbicacionService.class);
    
    private final RabbitTemplate rabbitTemplate;
    
    @Value("${rabbitmq.exchange.transporte}")
    private String exchange;
    
    @Value("${rabbitmq.routing.key.ubicaciones}")
    private String routingKey;
    
    public UbicacionService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    
    public void enviarUbicacion(UbicacionGPS ubicacion) {
        try {
            // Asignar fecha y hora actual si no viene
            if (ubicacion.getFechaHora() == null) {
                ubicacion.setFechaHora(LocalDateTime.now());
            }
            
            // Asignar estado por defecto si no viene
            if (ubicacion.getEstado() == null || ubicacion.getEstado().isEmpty()) {
                ubicacion.setEstado("EN_RUTA");
            }
            
            logger.info("Enviando ubicación del autobús {} a RabbitMQ", ubicacion.getIdAutobus());
            logger.debug("Datos de ubicación: {}", ubicacion);
            
            rabbitTemplate.convertAndSend(exchange, routingKey, ubicacion);
            
            logger.info("Ubicación enviada exitosamente");
        } catch (Exception e) {
            logger.error("Error al enviar ubicación a RabbitMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar ubicación: " + e.getMessage(), e);
        }
    }
}
