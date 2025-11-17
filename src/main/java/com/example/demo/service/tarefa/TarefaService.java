package com.example.demo.service.tarefa;

import com.example.demo.domain.model.dto.PageResponse;
import com.example.demo.domain.model.dto.tarefa.TarefaDTO;
import com.example.demo.domain.model.tarefa.CategoriaSustentabilidade;
import com.example.demo.domain.model.tarefa.MissaoSustentavel;
import com.example.demo.domain.model.tarefa.Tarefa;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.repository.CategoriaSustentabilidadeRepository;
import com.example.demo.repository.MissaoSustentavelRepository;
import com.example.demo.repository.TarefaRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    @Autowired
    TarefaRepository tarefaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    MissaoSustentavelRepository missaoSustentavelRepository;

    @Autowired
    CategoriaSustentabilidadeRepository categoriaSustentabilidadeRepository;

    /**
     * Lista todas as tarefas
     */
    @Cacheable(value = "tarefas", key = "'all'")
    public List<Tarefa> listarTodasTarefas() {
        System.out.println("🔍 [CACHE MISS] Buscando todas as tarefas no banco...");
        return tarefaRepository.findAll();
    }

    /**
     * Lista tarefas com paginação
     */
    @Cacheable(value = "tarefas", key = "'page:' + #page + ':size:' + #size")
    public PageResponse<Tarefa> listarTarefasPaginadas(int page, int size) {

        if (page < 0) throw new IllegalArgumentException("Page must be >= 0");
        if (size < 1 || size > 100)
            throw new IllegalArgumentException("Page size must be between 1 and 100");

        Pageable pageable = PageRequest.of(page, size);
        Page<Tarefa> tarefasPage = tarefaRepository.findAll(pageable);

        return new PageResponse<>(
                tarefasPage.getContent(),
                tarefasPage.getNumber(),
                tarefasPage.getSize(),
                tarefasPage.getTotalElements(),
                tarefasPage.getTotalPages(),
                tarefasPage.isFirst(),
                tarefasPage.isLast()
        );
    }

    /**
     * Busca tarefa por ID
     */
    @Cacheable(value = "tarefa", key = "#id")
    public Tarefa buscarTarefaPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + id));
    }

    /**
     * Cria nova tarefa e limpa o cache
     */
    @CacheEvict(value = "tarefas", allEntries = true)
    public Tarefa criarTarefa(TarefaDTO dto) {

        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setCompletado(dto.completado());
        tarefa.setDataCriacao(dto.dataCriacao());
        tarefa.setPoints(dto.points());

        MissaoSustentavel missao = missaoSustentavelRepository.findById(dto.missaoId())
                .orElseThrow(() -> new RuntimeException("Missão não encontrada com ID: " + dto.missaoId()));
        tarefa.setMissao(missao);

        CategoriaSustentabilidade categoria = categoriaSustentabilidadeRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + dto.categoriaId()));
        tarefa.setCategoria(categoria);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.usuarioId()));
        tarefa.setUsuario(usuario);

        return tarefaRepository.save(tarefa);
    }

    /**
     * Atualiza tarefa existente
     */
    @Caching(evict = {
            @CacheEvict(value = "tarefa", key = "#id"),
            @CacheEvict(value = "tarefas", allEntries = true)
    })
    public Tarefa atualizarTarefa(Long id, TarefaDTO dto) {

        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + id));

        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setCompletado(dto.completado());
        tarefa.setDataCriacao(dto.dataCriacao());
        tarefa.setPoints(dto.points());

        if (dto.categoriaId() != null) {
            CategoriaSustentabilidade categoria = categoriaSustentabilidadeRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + dto.categoriaId()));
            tarefa.setCategoria(categoria);
        }

        if (dto.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.usuarioId()));
            tarefa.setUsuario(usuario);
        }

        return tarefaRepository.save(tarefa);
    }

    /**
     * Remove uma tarefa
     */
    @Caching(evict = {
            @CacheEvict(value = "tarefa", key = "#id"),
            @CacheEvict(value = "tarefas", allEntries = true)
    })
    public void deletarTarefa(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada com ID: " + id);
        }
        tarefaRepository.deleteById(id);
    }

}
