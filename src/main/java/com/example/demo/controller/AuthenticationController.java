package com.example.demo.controller;

import com.example.demo.domain.model.dto.auth.AuthenticationDTO;
import com.example.demo.domain.model.dto.auth.LoginResponseDTO;
import com.example.demo.domain.model.dto.auth.RegisterDTO;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.infra.security.TokenService;
import com.example.demo.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TokenService tokenService;

    @Autowired
    MessageSource messageSource;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody @Valid RegisterDTO dto) {
        if (this.repository.findByEmail(dto.email()) != null) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", 400);
            error.put("message", messageSource.getMessage("auth.email.already.registered", null, LocaleContextHolder.getLocale()));
            error.put("timestamp", java.time.LocalDateTime.now());
            return ResponseEntity.badRequest().body(error);
        }

    String encryptedPassword = new BCryptPasswordEncoder().encode(dto.password());
        Usuario newUser = new Usuario();
        newUser.setUsername(dto.username());
        newUser.setEmail(dto.email());
        newUser.setPassword(encryptedPassword);
        newUser.setRole(dto.role());

        this.repository.save(newUser);
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", 200);
        response.put("message", messageSource.getMessage("auth.user.registered", null, LocaleContextHolder.getLocale()));
        response.put("timestamp", java.time.LocalDateTime.now());
        return ResponseEntity.ok(response);
    }

}
