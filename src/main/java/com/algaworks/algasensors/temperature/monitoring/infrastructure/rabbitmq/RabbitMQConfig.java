package com.algaworks.algasensors.temperature.monitoring.infrastructure.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    private static final String PROCESS_TEMPERATURE = "temperature-monitoring.process-temperature.v1";
    public static final String QUEUE_PROCESS_TEMPERATURE = PROCESS_TEMPERATURE + ".q";
    public static final String DEAD_LETTER_QUEUE_PROCESS_TEMPERATURE = PROCESS_TEMPERATURE + ".dlq";
    public static final String QUEUE_ALERTING = "temperature-monitoring.alerting.v1.q";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory factory) {
        return new RabbitAdmin(factory);
    }

    @Bean
    public Queue queueProcessTemperature() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "");
        args.put("x-dead-letter-routing-key", DEAD_LETTER_QUEUE_PROCESS_TEMPERATURE);
        return QueueBuilder.durable(QUEUE_PROCESS_TEMPERATURE).withArguments(args).build();
    }

    @Bean
    public Queue deadLetterQueueProcessTemperature() {
        return QueueBuilder.durable(DEAD_LETTER_QUEUE_PROCESS_TEMPERATURE).build();
    }

    @Bean
    public Queue queueAlerting() {
        return QueueBuilder.durable(QUEUE_ALERTING).build();
    }

    @Bean
    public Binding bindingProcessTemperature() {
        return BindingBuilder.bind(this.queueProcessTemperature()).to(this.existingExchange());
    }

    @Bean
    public Binding bindingAlerting() {
        return BindingBuilder.bind(this.queueAlerting()).to(this.existingExchange());
    }

    /*
     * Not a @Bean
     * Created by temperature-processing microservice
     */
    public FanoutExchange existingExchange() {
        return ExchangeBuilder.fanoutExchange("temperature-processing.temperature-received.v1.e").build();
    }
}
