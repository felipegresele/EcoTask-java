package com.example.demo.domain.model.tarefa;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MissaoSustentavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome da missão não pode ser vazio")
    private String nome;

    @NotBlank(message = "A descrição da missão não pode ser vazia")
    private String descricao;

    @NotNull(message = "A data de início deve ser informada")
    private LocalDate dataInicio;

    @NotNull(message = "A data de término deve ser informada")
    private LocalDate dataFim;

    private boolean ativa = true;

}
