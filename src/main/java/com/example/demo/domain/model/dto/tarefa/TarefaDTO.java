package com.example.demo.domain.model.dto.tarefa;

import java.time.LocalDate;

public record TarefaDTO(
        Long id,
        String titulo,
        String descricao,
        boolean completado,
        LocalDate dataCriacao,
        int points,
        Long categoriaId,
        Long usuarioId
) {
}
