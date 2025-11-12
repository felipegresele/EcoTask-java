package com.example.demo.service.tarefa;

import com.example.demo.domain.model.dto.tarefa.CategoriaSustentabilidadeDTO;
import com.example.demo.domain.model.tarefa.CategoriaSustentabilidade;
import com.example.demo.repository.CategoriaSustentabilidadeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaSustentabilidadeService {

    private final CategoriaSustentabilidadeRepository repository;

    public CategoriaSustentabilidadeService(CategoriaSustentabilidadeRepository repository) {
        this.repository = repository;
    }

    /**
     * Lista todas as categorias com cache
     * Cache: "categorias" - lista completa de categorias
     */
    @Cacheable(value = "categorias", key = "'all'")
    public List<CategoriaSustentabilidade> listarTodasCategorias() {
        System.out.println("🔍 [CACHE MISS] Buscando categorias no banco de dados...");
        List<CategoriaSustentabilidade> categorias = repository.findAll();
        System.out.println("✅ [CACHE MISS] Encontradas " + categorias.size() + " categorias no banco");
        return categorias;
    }

    /**
     * Cria uma nova categoria e invalida o cache da lista
     */
    @CacheEvict(value = "categorias", key = "'all'")
    public CategoriaSustentabilidade criarCategoria(CategoriaSustentabilidadeDTO dto) {
        System.out.println("🗑️ [CACHE EVICT] Invalidando cache de categorias (lista)");
        CategoriaSustentabilidade categoria = new CategoriaSustentabilidade();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setNivelImpacto(dto.nivelImpacto());
        return repository.save(categoria);
    }

    /**
     * Atualiza uma categoria e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "categoria", key = "#id"),
            @CacheEvict(value = "categorias", key = "'all'")
    })
    public CategoriaSustentabilidade atualizarCategoria(Long id, CategoriaSustentabilidadeDTO dto) {
        CategoriaSustentabilidade categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + id));
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        categoria.setNivelImpacto(dto.nivelImpacto());
        return repository.save(categoria);
    }

    /**
     * Deleta uma categoria e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "categoria", key = "#id"),
            @CacheEvict(value = "categorias", key = "'all'")
    })
    public void deletarCategoria(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Categoria não encontrada com ID: " + id);
        }
        repository.deleteById(id);
    }
}
