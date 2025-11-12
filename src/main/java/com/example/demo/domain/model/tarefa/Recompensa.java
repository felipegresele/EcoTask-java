package com.example.demo.domain.model.tarefa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da recompensa não pode ser vazio")
    private String nome;

    @NotBlank(message = "Descrição da recompensa não pode ser vazio")
    private String descricao;

    @Min(value = 1, message = "Os pontos requeridos devem ser pelo menos 1")
    @PositiveOrZero(message = "Os pontos requeridos não podem ser negativos")
    private int pontosRequiridos;

    private boolean ativado = true;

}
