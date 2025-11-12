package com.example.demo.infra.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TASK_EVENT_QUEUE = "tasks.events";
    public static final String TASK_EVENT_EXCHANGE = "tasks.exchange";
    public static final String TASK_EVENT_ROUTING_KEY = "tasks.created";

    @Bean
    public Queue taskEventQueue() {
        return new Queue(TASK_EVENT_QUEUE, true);
    }

    @Bean
    public DirectExchange taskEventExchange() {
        return new DirectExchange(TASK_EVENT_EXCHANGE);
    }

    @Bean
    public Binding taskEventBinding(Queue taskEventQueue, DirectExchange taskEventExchange) {
        return BindingBuilder.bind(taskEventQueue)
                .to(taskEventExchange)
                .with(TASK_EVENT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        template.setReplyTimeout(15000L);
        return template;
    }
}

