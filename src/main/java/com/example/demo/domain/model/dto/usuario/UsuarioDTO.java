package com.example.demo.domain.model.dto.usuario;

import com.example.demo.domain.model.usuario.UserRole;

public record UsuarioDTO (
        Long id,
        String username,
        String email,
        String password,
        UserRole role
){
}
