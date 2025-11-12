package com.example.demo.domain.model.dto.tarefa;

import jakarta.validation.constraints.NotBlank;

public record RecompensaDTO(
        Long id,
        String nome,
        String descricao,
        int pontosRequiridos,
        boolean ativado
){
}
