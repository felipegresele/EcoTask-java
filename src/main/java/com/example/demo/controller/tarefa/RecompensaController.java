package com.example.demo.controller.tarefa;

import com.example.demo.domain.model.dto.tarefa.CategoriaSustentabilidadeDTO;
import com.example.demo.domain.model.dto.tarefa.RecompensaDTO;
import com.example.demo.domain.model.tarefa.CategoriaSustentabilidade;
import com.example.demo.domain.model.tarefa.Recompensa;
import com.example.demo.service.tarefa.CategoriaSustentabilidadeService;
import com.example.demo.service.tarefa.RecompensaService;
import dev.langchain4j.service.spring.AiService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recompensas")
public class RecompensaController {

        @Autowired
        RecompensaService service;

        @GetMapping
        public List<Recompensa> listarTodas() {
            return service.listarTodasRecompensas();
        }

        @PostMapping
        public ResponseEntity<Recompensa> criar(@RequestBody @Valid RecompensaDTO dto) {
            return ResponseEntity.ok(service.criarRecompensa(dto));
        }

        @PutMapping("/{id}")
        public ResponseEntity<Recompensa> atualizar(@PathVariable Long id, @RequestBody @Valid RecompensaDTO dto) {
            return ResponseEntity.ok(service.atualizarRecompensa(id, dto));
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletar(@PathVariable Long id) {
            service.deletarRecompensa(id);
            return ResponseEntity.noContent().build();
        }
}
