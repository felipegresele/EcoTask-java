package com.example.demo.service.tarefa;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.demo.domain.model.dto.tarefa.TarefaCriadaEvent;
import com.example.demo.infra.config.RabbitMQConfig;

@Component
public class TaskEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public TaskEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTaskCreated(TarefaCriadaEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.TASK_EVENT_EXCHANGE,
                RabbitMQConfig.TASK_EVENT_ROUTING_KEY,
                event);
    }
}

