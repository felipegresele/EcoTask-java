package com.example.demo.domain.model.dto.tarefa;

import java.time.LocalDate;

public record MissaoSustentavelDTO(
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataFim,
        boolean ativa
) {}
