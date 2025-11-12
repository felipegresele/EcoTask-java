package com.example.demo.service.tarefa;

import com.example.demo.domain.model.dto.PageResponse;
import com.example.demo.domain.model.dto.tarefa.TarefaDTO;
import com.example.demo.domain.model.dto.tarefa.TarefaCriadaEvent;
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

import java.time.OffsetDateTime;
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

    @Autowired
    TaskEventPublisher taskEventPublisher;

    /**
     * Lista todas as tarefas com cache (método sem paginação - mantido para compatibilidade)
     * Cache: "tarefas" - lista completa de tarefas
     */
    @Cacheable(value = "tarefas", key = "'all'")
    public List<Tarefa> listarTodasTarefas() {
        System.out.println("🔍 [CACHE MISS] Buscando tarefas no banco de dados...");
        List<Tarefa> tarefas = tarefaRepository.findAll();
        System.out.println("✅ [CACHE MISS] Encontradas " + tarefas.size() + " tarefas no banco");
        return tarefas;
    }

    /**
     * Lista tarefas com paginação
     * Cache: "tarefas" - página específica de tarefas
     * @param page Número da página (0-indexed)
     * @param size Tamanho da página
     * @return PageResponse com as tarefas paginadas
     */
    @Cacheable(value = "tarefas", key = "'page:' + #page + ':size:' + #size")
    public PageResponse<Tarefa> listarTarefasPaginadas(int page, int size) {
        System.out.println("🔍 [CACHE MISS] Buscando tarefas paginadas (página " + page + ", tamanho " + size + ") no banco de dados...");
        
        // Validação de parâmetros
        if (page < 0) {
            throw new IllegalArgumentException("Page number must be greater than or equal to 0");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Page size must be between 1 and 100");
        }
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Tarefa> tarefasPage = tarefaRepository.findAll(pageable);
        
        System.out.println("✅ [CACHE MISS] Encontradas " + tarefasPage.getNumberOfElements() + " tarefas na página " + page + " de " + tarefasPage.getTotalPages());
        
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
     * Busca tarefa por ID com cache
     * Cache: "tarefa" - tarefa individual por ID
     */
    @Cacheable(value = "tarefa", key = "#id")
    public Tarefa buscarTarefaPorId(Long id) {
        System.out.println("🔍 [CACHE MISS] Buscando tarefa ID " + id + " no banco de dados...");
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada com ID: " + id));
        System.out.println("✅ [CACHE MISS] Tarefa encontrada: " + tarefa.getTitulo());
        return tarefa;
    }

    /**
     * Cria uma nova tarefa e invalida o cache da lista e páginas
     * @CacheEvict: Remove todas as entradas do cache de tarefas quando uma nova é criada
     */
    @CacheEvict(value = "tarefas", allEntries = true)
    public Tarefa criarTarefa(TarefaDTO dto) {
        System.out.println("🗑️ [CACHE EVICT] Invalidando cache de tarefas (todas as entradas)");
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo(dto.titulo());
        tarefa.setDescricao(dto.descricao());
        tarefa.setCompletado(dto.completado());
        tarefa.setDataCriacao(dto.dataCriacao());
        tarefa.setPoints(dto.points());

        MissaoSustentavel missaoSustentavel = missaoSustentavelRepository.findById(dto.missaoId())
                .orElseThrow(() -> new RuntimeException("Missão não encontrada com ID: " + dto.missaoId()));
        tarefa.setMissao(missaoSustentavel);

        CategoriaSustentabilidade categoria = categoriaSustentabilidadeRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + dto.categoriaId()));
        tarefa.setCategoria(categoria);

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + dto.usuarioId()));
        tarefa.setUsuario(usuario);

        Tarefa tarefaSalva = tarefaRepository.save(tarefa);

        TarefaCriadaEvent event = new TarefaCriadaEvent(
                tarefaSalva.getId(),
                tarefaSalva.getTitulo(),
                tarefaSalva.getUsuario().getId(),
                tarefaSalva.getCategoria().getId(),
                OffsetDateTime.now()
        );
        taskEventPublisher.publishTaskCreated(event);

        return tarefaSalva;
    }

    /**
     * Atualiza uma tarefa e invalida os caches relacionados
     * @Caching: Remove tanto o cache individual quanto todas as páginas
     */
    @Caching(evict = {
            @CacheEvict(value = "tarefa", key = "#id"),
            @CacheEvict(value = "tarefas", allEntries = true)
    })
    public Tarefa atualizarTarefa(Long id, TarefaDTO dto) {
        System.out.println("🗑️ [CACHE EVICT] Invalidando cache de tarefa ID " + id + " e todas as páginas de tarefas");
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
     * Deleta uma tarefa e invalida os caches relacionados
     */
    @Caching(evict = {
            @CacheEvict(value = "tarefa", key = "#id"),
            @CacheEvict(value = "tarefas", allEntries = true)
    })
    public void deletarTarefa(Long id) {
        System.out.println("🗑️ [CACHE EVICT] Invalidando cache de tarefa ID " + id + " e todas as páginas de tarefas");
        if (!tarefaRepository.existsById(id)) {
            throw new RuntimeException("Tarefa não encontrada com ID: " + id);
        }
        tarefaRepository.deleteById(id);
    }

}
