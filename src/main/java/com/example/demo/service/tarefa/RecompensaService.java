package com.example.demo.service.tarefa;

import com.example.demo.domain.model.dto.tarefa.CategoriaSustentabilidadeDTO;
import com.example.demo.domain.model.dto.tarefa.RecompensaDTO;
import com.example.demo.domain.model.tarefa.CategoriaSustentabilidade;
import com.example.demo.domain.model.tarefa.Recompensa;
import com.example.demo.repository.CategoriaSustentabilidadeRepository;
import com.example.demo.repository.RecompensaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecompensaService {

    @Autowired
    RecompensaRepository repository;

    /**
     * Lista todas as recompensas com cache
     * Cache: "recompensas" - lista completa de recompensas
     */
    @Cacheable(value = "recompensas", key = "'all'")
    public List<Recompensa> listarTodasRecompensas() {
        System.out.println("🔍 [CACHE MISS] Buscando recompensas no banco de dados...");
        List<Recompensa> recompensas = repository.findAll();
        System.out.println("✅ [CACHE MISS] Encontradas " + recompensas.size() + " recompensas no banco");
        return recompensas;
    }

    /**
     * Cria uma nova recompensa e invalida o cache da lista
     */
    @CacheEvict(value = "recompensas", key = "'all'")
    public Recompensa criarRecompensa(RecompensaDTO dto) {
        System.out.println("🗑️ [CACHE EVICT] Invalidando cache de recompensas (lista)");
        Recompensa recompensa = new Recompensa();
        recompensa.setNome(dto.nome());
        recompensa.setDescricao(dto.descricao());
        recompensa.setAtivado(dto.ativado());
        recompensa.setPontosRequiridos(dto.pontosRequiridos());
        return repository.save(recompensa);
    }

    /**
     * Atualiza uma recompensa e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "recompensa", key = "#id"),
            @CacheEvict(value = "recompensas", key = "'all'")
    })
    public Recompensa atualizarRecompensa(Long id, RecompensaDTO dto) {
        Recompensa recompensa = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recompensa não encontrada com ID: " + id));
        recompensa.setNome(dto.nome());
        recompensa.setDescricao(dto.descricao());
        recompensa.setAtivado(dto.ativado());
        recompensa.setPontosRequiridos(dto.pontosRequiridos());
        return repository.save(recompensa);
    }

    /**
     * Deleta uma recompensa e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "recompensa", key = "#id"),
            @CacheEvict(value = "recompensas", key = "'all'")
    })
    public void deletarRecompensa(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Recompensa não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
