package com.example.demo.domain.model.dto.auth;

import com.example.demo.domain.model.usuario.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "Username é obrigatório")
        @Size(min = 3, max = 30, message = "Username deve ter entre 3 e 30 caracteres")
        String username,
        
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email deve ser válido")
        String email,
        
        @NotBlank(message = "Password é obrigatório")
        @Size(min = 6, message = "Password deve ter no mínimo 6 caracteres")
        String password,
        
        @NotNull(message = "Role é obrigatória")
        UserRole role
) {
}
