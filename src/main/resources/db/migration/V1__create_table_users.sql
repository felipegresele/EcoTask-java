CREATE TABLE usuario (
    id SERIAL PRIMARY KEY,
    username VARCHAR(30) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    role TEXT NOT NULL
);