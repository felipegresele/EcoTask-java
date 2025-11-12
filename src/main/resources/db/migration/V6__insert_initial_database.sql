-- Usuários iniciais
INSERT INTO usuario (username, email, password, role) VALUES
('admin', 'admin@tarefasustentavel.com', '123456', 'ADMIN'),
('felipe', 'felipe@tarefasustentavel.com', '123456', 'USER');

-- Categorias de Sustentabilidade
INSERT INTO categoria_sustentabilidade (nome, descricao, nivel_impacto) VALUES
('Reciclagem', 'Categoria voltada à separação e reaproveitamento de materiais recicláveis.', 'ALTO'),
('Economia de Energia', 'Incentivo a práticas que reduzem o consumo elétrico no ambiente de trabalho.', 'MEDIO'),
('Consumo Consciente', 'Promoção de hábitos de compra sustentáveis e redução de desperdício.', 'BAIXO');

-- Missões Sustentáveis
INSERT INTO missao_sustentavel (nome, descricao, data_inicio, data_fim, ativa) VALUES
('Missão Verde Semanal', 'Complete tarefas de reciclagem e consumo consciente durante a semana.', CURRENT_DATE, CURRENT_DATE + INTERVAL '7 days', TRUE),
('Energia Limpa', 'Desafie-se a reduzir o consumo de energia elétrica no ambiente doméstico.', CURRENT_DATE, CURRENT_DATE + INTERVAL '10 days', TRUE),
('Consumo Inteligente', 'Adote hábitos mais sustentáveis e evite desperdícios no dia a dia.', CURRENT_DATE, CURRENT_DATE + INTERVAL '5 days', TRUE);

-- Tarefas sustentáveis (ligadas às missões e categorias)
INSERT INTO tarefa (titulo, descricao, completado, data_criacao, points, categoria_id, usuario_id, missao_id)
VALUES
('Separar o lixo reciclável', 'Organize os resíduos recicláveis corretamente.', FALSE, CURRENT_DATE, 10, 1, 2, 1),
('Desligar luzes ao sair', 'Apague as luzes e aparelhos quando não estiverem em uso.', TRUE, CURRENT_DATE, 15, 2, 2, 2),
('Usar caneca reutilizável', 'Evite copos descartáveis e use uma caneca própria.', FALSE, CURRENT_DATE, 5, 3, 2, 3),
('Iniciar coleta seletiva', 'Monte um sistema de separação de lixo em casa ou no trabalho.', FALSE, CURRENT_DATE, 12, 1, 2, 1),
('Evitar uso de ar-condicionado', 'Tente usar ventilação natural quando possível.', FALSE, CURRENT_DATE, 20, 2, 2, 2);

-- Recompensas
INSERT INTO recompensa (nome, descricao, pontos_requiridos, ativado) VALUES
('Certificado Verde', 'Reconhecimento simbólico por práticas sustentáveis consistentes.', 50, TRUE),
('Dia de Folga Sustentável', 'Um dia de descanso adicional por participação contínua em tarefas sustentáveis.', 100, TRUE),
('Guerreiro Eco', 'Complete 15 tarefas sustentáveis na semana.', 150, TRUE),
('Guardião da Natureza', 'Conclua todas as missões mensais com sucesso.', 200, TRUE);
