package com.example.demo.controller.tarefa;

import com.example.demo.domain.model.dto.tarefa.CategoriaSustentabilidadeDTO;
import com.example.demo.domain.model.tarefa.CategoriaSustentabilidade;
import com.example.demo.service.tarefa.CategoriaSustentabilidadeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaSustentabilidadeController {

    private final CategoriaSustentabilidadeService service;

    public CategoriaSustentabilidadeController(CategoriaSustentabilidadeService service) {
        this.service = service;
    }

    @GetMapping
    public List<CategoriaSustentabilidade> listarTodas() {
        return service.listarTodasCategorias();
    }

    @PostMapping
    public ResponseEntity<CategoriaSustentabilidade> criar(@RequestBody CategoriaSustentabilidadeDTO dto) {
        return ResponseEntity.ok(service.criarCategoria(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaSustentabilidade> atualizar(@PathVariable Long id, @RequestBody CategoriaSustentabilidadeDTO dto) {
        return ResponseEntity.ok(service.atualizarCategoria(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}