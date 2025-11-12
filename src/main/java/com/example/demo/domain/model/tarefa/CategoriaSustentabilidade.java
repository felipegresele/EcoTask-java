package com.example.demo.domain.model.tarefa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaSustentabilidade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da categoria não pode ser vazio")
    private String nome;

    @NotBlank(message = "Descrição da categoria não pode ser vazio")
    private String descricao;

    @NotNull(message = "Nível de impacto da categoria não pode ser nulo")
    @Enumerated(EnumType.STRING)
    private Nivel nivelImpacto;

}
