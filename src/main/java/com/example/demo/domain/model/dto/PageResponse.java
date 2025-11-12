package com.example.demo.domain.model.dto;

import java.util.List;

/**
 * DTO para resposta paginada
 * @param <T> Tipo dos itens na página
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {
    public PageResponse {
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Page cannot be negative");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
    }
}

