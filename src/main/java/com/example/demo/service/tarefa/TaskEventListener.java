package com.example.demo.service.tarefa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.demo.domain.model.dto.tarefa.TarefaCriadaEvent;
import com.example.demo.infra.config.RabbitMQConfig;

@Component
public class TaskEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskEventListener.class);

    @RabbitListener(queues = RabbitMQConfig.TASK_EVENT_QUEUE)
    public void handleTaskCreated(TarefaCriadaEvent event) {
        LOGGER.info("📬 Mensagem recebida do RabbitMQ: nova tarefa {} criada para o usuário {}", event.titulo(), event.usuarioId());
    }
}

