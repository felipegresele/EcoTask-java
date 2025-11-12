package com.example.demo.controller;

import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheManager cacheManager;

    public CacheController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Limpa todos os caches
     * GET /cache/clear
     */
    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            if (cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).clear();
            }
        });
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Todos os caches foram limpos com sucesso");
        return ResponseEntity.ok(response);
    }

    /**
     * Limpa um cache específico
     * DELETE /cache/{cacheName}
     */
    @DeleteMapping("/{cacheName}")
    public ResponseEntity<Map<String, String>> clearCache(@PathVariable String cacheName) {
        if (cacheManager.getCache(cacheName) != null) {
            cacheManager.getCache(cacheName).clear();
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cache '" + cacheName + "' foi limpo com sucesso");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Cache '" + cacheName + "' não encontrado");
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Lista todos os caches disponíveis
     * GET /cache
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> listCaches() {
        Map<String, Object> response = new HashMap<>();
        response.put("caches", cacheManager.getCacheNames());
        response.put("total", cacheManager.getCacheNames().size());
        return ResponseEntity.ok(response);
    }
}

