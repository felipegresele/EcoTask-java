CREATE TABLE tarefa (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    completado BOOLEAN DEFAULT FALSE,
    data_criacao DATE,
    points INT DEFAULT 10,

    categoria_id BIGINT,
    usuario_id BIGINT,

    CONSTRAINT fk_tarefa_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria_sustentabilidade (id)
        ON DELETE SET NULL
        ON UPDATE CASCADE,

    CONSTRAINT fk_tarefa_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);