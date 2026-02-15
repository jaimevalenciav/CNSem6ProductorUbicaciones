package com.transporte.ubicaciones.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Value("${rabbitmq.queue.ubicaciones}")
    private String queueName;
    
    @Value("${rabbitmq.exchange.transporte}")
    private String exchange;
    
    @Value("${rabbitmq.routing.key.ubicaciones}")
    private String routingKey;
    
    @Bean
    public Queue ubicacionesQueue() {
        return new Queue(queueName, true); // durable = true
    }
    
    @Bean
    public TopicExchange transporteExchange() {
        return new TopicExchange(exchange);
    }
    
    @Bean
    public Binding binding(Queue ubicacionesQueue, TopicExchange transporteExchange) {
        return BindingBuilder
                .bind(ubicacionesQueue)
                .to(transporteExchange)
                .with(routingKey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}
