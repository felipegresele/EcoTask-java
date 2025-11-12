package com.example.demo.service.usuario;

import com.example.demo.domain.model.dto.usuario.UsuarioDTO;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository repository;

    /**
     * Lista todos os usuários com cache
     * Cache: "usuarios" - lista completa de usuários
     */
    @Cacheable(value = "usuarios", key = "'all'")
    public List<Usuario> listarTodosUsuarios() {
        System.out.println("🔍 [CACHE MISS] Buscando usuários no banco de dados...");
        List<Usuario> usuarios = repository.findAll();
        System.out.println("✅ [CACHE MISS] Encontrados " + usuarios.size() + " usuários no banco");
        return usuarios;
    }

    /**
     * Busca usuário por ID (sem cache por questões de segurança)
     * Não usamos cache para usuários individuais devido a dados sensíveis
     */
    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
    }

    /**
     * Atualiza um usuário e invalida o cache da lista
     */
    @CacheEvict(value = "usuarios", key = "'all'")
    public Usuario editarUsuario(Long id, UsuarioDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com ID: " + id));
        
        usuario.setUsername(dto.username());
        usuario.setEmail(dto.email());
        
        // Criptografar a senha se fornecida
        if (dto.password() != null && !dto.password().isEmpty()) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
            usuario.setPassword(encryptedPassword);
        }
        
        usuario.setRole(dto.role());
        return repository.save(usuario);
    }

    /**
     * Deleta um usuário e invalida o cache da lista
     */
    @CacheEvict(value = "usuarios", key = "'all'")
    public void excluirUsuario(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}
