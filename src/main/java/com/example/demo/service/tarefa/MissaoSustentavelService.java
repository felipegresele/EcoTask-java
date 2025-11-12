package com.example.demo.service.tarefa;

import com.example.demo.domain.model.dto.tarefa.MissaoSustentavelDTO;
import com.example.demo.domain.model.tarefa.MissaoSustentavel;
import com.example.demo.repository.MissaoSustentavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MissaoSustentavelService {

    @Autowired
    private MissaoSustentavelRepository repository;

    /**
     * Lista todas as missões sustentáveis com cache
     * Cache: "missoes" - lista completa de missões
     */
    @Cacheable(value = "missoes", key = "'all'")
    public List<MissaoSustentavel> listarTodasMissoesSustentaveis() {
        System.out.println("🔍 [CACHE MISS] Buscando missões no banco de dados...");
        List<MissaoSustentavel> missoes = repository.findAll();
        System.out.println("✅ [CACHE MISS] Encontradas " + missoes.size() + " missões no banco");
        return missoes;
    }

    /**
     * Cria uma nova missão sustentável e invalida o cache da lista
     */
    @CacheEvict(value = "missoes", key = "'all'")
    public MissaoSustentavel criarMissaoSustentavel(MissaoSustentavelDTO dto) {
        System.out.println("🗑️ [CACHE EVICT] Invalidando cache de missões (lista)");
        MissaoSustentavel missao = new MissaoSustentavel();
        missao.setNome(dto.nome());
        missao.setDescricao(dto.descricao());
        missao.setDataInicio(dto.dataInicio());
        missao.setDataFim(dto.dataFim());
        missao.setAtiva(dto.ativa());
        return repository.save(missao);
    }

    /**
     * Atualiza uma missão sustentável e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "missao", key = "#id"),
            @CacheEvict(value = "missoes", key = "'all'")
    })
    public MissaoSustentavel atualizarMissaoSustentavel(Long id, MissaoSustentavelDTO dto) {
        MissaoSustentavel missao = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Missão não encontrada com ID: " + id));

        missao.setNome(dto.nome());
        missao.setDescricao(dto.descricao());
        missao.setDataInicio(dto.dataInicio());
        missao.setDataFim(dto.dataFim());
        missao.setAtiva(dto.ativa());

        return repository.save(missao);
    }

    /**
     * Deleta uma missão sustentável e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "missao", key = "#id"),
            @CacheEvict(value = "missoes", key = "'all'")
    })
    public void deletarMissaoSustentavel(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Missão não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}