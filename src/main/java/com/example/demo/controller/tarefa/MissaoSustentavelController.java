package com.example.demo.controller.tarefa;

import com.example.demo.domain.model.dto.tarefa.MissaoSustentavelDTO;
import com.example.demo.domain.model.tarefa.MissaoSustentavel;
import com.example.demo.service.tarefa.MissaoSustentavelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/missoes")
public class MissaoSustentavelController {

    @Autowired
    private MissaoSustentavelService service;

    @GetMapping
    public List<MissaoSustentavel> listarTodas() {
        return service.listarTodasMissoesSustentaveis();
    }

    @PostMapping
    public ResponseEntity<MissaoSustentavel> criar(@RequestBody @Valid MissaoSustentavelDTO dto) {
        return ResponseEntity.ok(service.criarMissaoSustentavel(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MissaoSustentavel> atualizar(@PathVariable Long id, @RequestBody @Valid MissaoSustentavelDTO dto) {
        return ResponseEntity.ok(service.atualizarMissaoSustentavel(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarMissaoSustentavel(id);
        return ResponseEntity.noContent().build();
    }
}
