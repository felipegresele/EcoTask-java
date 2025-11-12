package com.example.demo.infra.security;

import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //pegar o token e recuperar as informações dele
        var token = this.recoverToken(request);
        if (token != null && !token.isEmpty()) {
            try {
                var email = tokenService.validateToken(token);
                // Só autentica se o token for válido (email não está vazio)
                if (email != null && !email.isEmpty()) {
                    // Buscar o usuário completo do banco usando o método específico
                    // Isso garante que o campo 'role' seja carregado corretamente
                    var usuarioOptional = userRepository.findUsuarioByEmail(email);
                    
                    if (usuarioOptional.isPresent()) {
                        Usuario usuario = usuarioOptional.get();
                        // Verificar se o usuário tem role (não é null)
                        if (usuario.getRole() != null) {
                            // Garantir que o usuário tenha as authorities corretas
                            var authentication = new UsernamePasswordAuthenticationToken(
                                    usuario, 
                                    null, 
                                    usuario.getAuthorities()
                            );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            } catch (Exception e) {
                // Se houver erro na validação do token, não autentica
                // Mas continua com a requisição (pode ser um endpoint público)
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isEmpty()) {
            return null;
        }
        // Remove "Bearer " do início do header (case insensitive)
        if (authHeader.startsWith("Bearer ") || authHeader.startsWith("bearer ")) {
            return authHeader.substring(7).trim();
        }
        return authHeader.trim();
    }
}
