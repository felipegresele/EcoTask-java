package com.example.demo.domain.model.tarefa;

import com.example.demo.domain.model.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Titúlo da tarefa não pode ser vazio")
    private String titulo;

    @NotBlank(message = "Descrição da tarefa não pode ser vazio")
    private String descricao;

    private boolean completado = false;

    @NotNull(message = "Data de criação da tarefa não pode ser nula")
    private LocalDate dataCriacao;

    @Min(value = 1, message = "Os pontos devem ser pelo menos 1")
    @PositiveOrZero(message = "Os pontos não podem ser negativos")
    private int points = 10;

    @ManyToOne
    @JoinColumn(name = "missao_id")
    private MissaoSustentavel missao;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaSustentabilidade categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
