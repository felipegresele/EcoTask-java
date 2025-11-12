package com.example.demo.domain.model.dto.tarefa;

import com.example.demo.domain.model.tarefa.Nivel;

public record CategoriaSustentabilidadeDTO(
        Long id,
        String nome,
        String descricao,
        Nivel nivelImpacto
) {
}
