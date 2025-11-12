CREATE TABLE categoria_sustentabilidade (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    nivel_impacto VARCHAR(50)
);