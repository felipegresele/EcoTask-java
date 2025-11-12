package com.example.demo.infra.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    /**
     * Configuração do CacheManager usando Caffeine
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                "tarefas",           // Cache para lista de tarefas
                "tarefa",            // Cache para tarefa individual
                "categorias",        // Cache para lista de categorias
                "categoria",         // Cache para categoria individual
                "recompensa",
                "recompensas",
                "missao",
                "missoes",
                "usuarios"           // Cache para lista de usuários
        );
        
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    /**
     * Configuração do Caffeine Cache
     * - Tamanho máximo: 500 itens por cache
     * - TTL (Time To Live): 10 minutos
     * - Expira após acesso: 5 minutos (se não for acessado)
     */
    private Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .maximumSize(500)                    // Máximo de 500 itens no cache
                .expireAfterWrite(10, TimeUnit.MINUTES)  // Expira após 10 minutos sem escrita
                .expireAfterAccess(5, TimeUnit.MINUTES)  // Expira após 5 minutos sem acesso
                .recordStats();                      // Registra estatísticas do cache
    }
}

