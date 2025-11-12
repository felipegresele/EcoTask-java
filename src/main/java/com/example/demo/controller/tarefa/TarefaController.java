package com.example.demo.controller.tarefa;

import com.example.demo.domain.model.dto.PageResponse;
import com.example.demo.domain.model.dto.tarefa.TarefaDTO;
import com.example.demo.domain.model.tarefa.Tarefa;
import com.example.demo.service.tarefa.TarefaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    TarefaService service;

    /**
     * Lista todas as tarefas sem paginação
     * Para usar paginação, use o endpoint /tarefas/paginated
     */
    @GetMapping
    public ResponseEntity<List<Tarefa>> listarTodas() {
        return ResponseEntity.ok(service.listarTodasTarefas());
    }

    /**
     * Lista tarefas com paginação
     * @param page Número da página (padrão: 0)
     * @param size Tamanho da página (padrão: 10, máximo: 100)
     * @return PageResponse com as tarefas paginadas
     */
    @GetMapping("/paginated")
    public ResponseEntity<PageResponse<Tarefa>> listarPaginadas(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(service.listarTarefasPaginadas(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarTarefaPorId(id));
    }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@RequestBody @Valid TarefaDTO dto) {
        return ResponseEntity.ok(service.criarTarefa(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizar(@PathVariable Long id, @RequestBody @Valid TarefaDTO dto) {
        return ResponseEntity.ok(service.atualizarTarefa(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarTarefa(id);
        return ResponseEntity.noContent().build();
    }
}