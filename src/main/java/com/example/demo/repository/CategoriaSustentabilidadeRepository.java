package com.example.demo.repository;

import com.example.demo.domain.model.tarefa.CategoriaSustentabilidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaSustentabilidadeRepository extends JpaRepository<CategoriaSustentabilidade, Long> {
}
