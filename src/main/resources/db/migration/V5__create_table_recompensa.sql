CREATE TABLE recompensa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT NOT NULL,
    pontos_requiridos INT NOT NULL DEFAULT 10,
    ativado BOOLEAN DEFAULT TRUE
);