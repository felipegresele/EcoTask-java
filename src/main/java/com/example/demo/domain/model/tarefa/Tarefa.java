package com.example.demo.domain.model.tarefa;

import com.example.demo.domain.model.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    private String descricao;

    private boolean completado = false;

    private LocalDate dataCriacao;

    private int points = 10;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaSustentabilidade categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

}
