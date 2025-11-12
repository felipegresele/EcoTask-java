package com.example.demo.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity //desabilita a configraucao do web security e eu configuro como vai funcionar
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Habilita CORS
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite todos os endpoints de auth sem autenticação
                        .requestMatchers("/auth/**").permitAll()
                        // Endpoints de cache - apenas ADMIN
                        .requestMatchers("/cache/**").hasRole("ADMIN")
                        // Operações de escrita (POST, PUT, DELETE) precisam de ADMIN
                        .requestMatchers(HttpMethod.POST, "/tarefas", "/tarefas/**", "/categorias", "/categorias/**", "/usuarios", "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tarefas/**", "/categorias/**", "/usuarios", "/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/tarefas/**", "/categorias/**", "/usuarios", "/usuarios/**").hasRole("ADMIN")
                        // Operações de leitura (GET) precisam apenas de autenticação
                        .requestMatchers(HttpMethod.GET, "/tarefas", "/tarefas/**", "/categorias", "/categorias/**", "/usuarios", "/usuarios/**").authenticated()
                        // Qualquer outra requisição precisa de autenticação
                        .anyRequest().authenticated())
                .httpBasic(httpBasic -> httpBasic.disable()) // Desabilita HTTP Basic
                .formLogin(formLogin -> formLogin.disable()) // Desabilita form login
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // Em produção, especifique as origens permitidas
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(false); // Deve ser false quando allowedOrigins é "*"

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
