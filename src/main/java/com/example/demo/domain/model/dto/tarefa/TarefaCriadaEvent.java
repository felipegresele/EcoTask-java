package com.example.demo.domain.model.dto.tarefa;

import java.time.OffsetDateTime;

public record TarefaCriadaEvent(
        Long tarefaId,
        String titulo,
        Long usuarioId,
        Long categoriaId,
        OffsetDateTime dataCriacao
) {
}

