# 🧪 Teste de Cache - Passo a Passo

## 📺 Como Ver os Logs no Console

Quando você rodar a aplicação, verá mensagens no console indicando quando o cache é usado:

- 🔍 `[CACHE MISS]` = Cache não encontrou, foi buscar no banco
- ✅ `[CACHE MISS]` = Dados encontrados no banco
- 🗑️ `[CACHE EVICT]` = Cache foi invalidado/limpo
- ⚡ **(Sem mensagem)** = Dados vieram do cache (muito rápido!)

---

## 🎯 Teste Prático: Lista de Tarefas

### Passo 1: Primeira Requisição (Vai ao Banco)

**No Postman:**
```
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

**No Console da Aplicação, você verá:**
```
🔍 [CACHE MISS] Buscando tarefas no banco de dados...
✅ [CACHE MISS] Encontradas X tarefas no banco
```

**Tempo de resposta:** ~150ms (vai ao banco)

---

### Passo 2: Segunda Requisição (Vem do Cache)

**No Postman (mesma requisição):**
```
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

**No Console da Aplicação:**
```
(Nenhuma mensagem - veio do cache!)
```

**Tempo de resposta:** ~5ms (vem do cache - MUITO mais rápido!)

---

### Passo 3: Criar Nova Tarefa (Invalidar Cache)

**No Postman:**
```
POST http://localhost:8080/tarefas
Authorization: Bearer {seu_token_admin}
Content-Type: application/json

{
  "titulo": "Nova tarefa",
  "descricao": "Teste",
  "completado": false,
  "dataCriacao": "2024-01-15",
  "points": 10,
  "categoriaId": 1,
  "usuarioId": 1
}
```

**No Console da Aplicação:**
```
🗑️ [CACHE EVICT] Invalidando cache de tarefas (lista)
```

**O que acontece:** Cache da lista foi removido!

---

### Passo 4: Listar Tarefas Novamente (Vai ao Banco)

**No Postman:**
```
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

**No Console da Aplicação:**
```
🔍 [CACHE MISS] Buscando tarefas no banco de dados...
✅ [CACHE MISS] Encontradas X+1 tarefas no banco
```

**Por que?** Porque o cache foi invalidado, então busca no banco novamente (incluindo a nova tarefa)

---

## 📊 Comparação: Com vs Sem Cache

### SEM Cache (Antes):
```
Requisição 1: GET /tarefas → Banco (150ms)
Requisição 2: GET /tarefas → Banco (150ms)
Requisição 3: GET /tarefas → Banco (150ms)
Requisição 4: GET /tarefas → Banco (150ms)
...
Todas as requisições vão ao banco!
```

### COM Cache (Agora):
```
Requisição 1: GET /tarefas → Banco (150ms) → Armazena no cache
Requisição 2: GET /tarefas → Cache (5ms) ⚡
Requisição 3: GET /tarefas → Cache (5ms) ⚡
Requisição 4: GET /tarefas → Cache (5ms) ⚡
...
Apenas a primeira vai ao banco!
```

**Resultado:** Performance melhorada em ~30x! 🚀

---

## 🔄 Fluxo Visual do Cache

### Cenário 1: Primeira Leitura
```
Cliente → GET /tarefas
    ↓
Spring verifica cache → ❌ NÃO ENCONTROU
    ↓
Executa método listarTodasTarefas()
    ↓
Banco de dados → SELECT * FROM tarefa
    ↓
Armazena no cache (memória)
    ↓
Retorna para cliente
```

### Cenário 2: Segunda Leitura (Cache Hit)
```
Cliente → GET /tarefas
    ↓
Spring verifica cache → ✅ ENCONTROU!
    ↓
Retorna do cache (MUITO RÁPIDO!)
    ↓
Cliente recebe resposta
```

### Cenário 3: Criar Nova Tarefa
```
Cliente → POST /tarefas
    ↓
Salva no banco de dados
    ↓
@CacheEvict → Remove cache da lista
    ↓
Próxima leitura vai buscar no banco
```

---

## 🧪 Teste Completo no Postman

### Sequência de Testes:

1. **Listar tarefas (1ª vez)**
   - Verá log: `[CACHE MISS]`
   - Tempo: ~150ms

2. **Listar tarefas (2ª vez)**
   - **NÃO** verá log
   - Tempo: ~5ms (muito mais rápido!)

3. **Criar tarefa**
   - Verá log: `[CACHE EVICT]`
   - Cache invalidado

4. **Listar tarefas (3ª vez)**
   - Verá log: `[CACHE MISS]` (cache foi limpo)
   - Tempo: ~150ms
   - Nova tarefa aparece na lista

5. **Listar tarefas (4ª vez)**
   - **NÃO** verá log
   - Tempo: ~5ms
   - Dados atualizados vêm do cache

---

## 📝 O Que Mudou no Código

### ANTES (Sem Cache):
```java
public List<Tarefa> listarTodasTarefas() {
    return tarefaRepository.findAll(); // Sempre vai ao banco
}
```

**Comportamento:**
- ✅ Sempre busca no banco
- ❌ Mais lento
- ❌ Mais carga no banco

### DEPOIS (Com Cache):
```java
@Cacheable(value = "tarefas", key = "'all'")
public List<Tarefa> listarTodasTarefas() {
    System.out.println("🔍 [CACHE MISS] Buscando tarefas no banco de dados...");
    return tarefaRepository.findAll(); // Vai ao banco apenas na primeira vez
}
```

**Comportamento:**
- ✅ Primeira vez: Vai ao banco
- ✅ Próximas vezes: Vem do cache (muito rápido!)
- ✅ Após criar/atualizar: Cache é invalidado, próxima busca vai ao banco

---

## 🎯 Como Saber se Está Funcionando

### Método 1: Ver Logs no Console

**Se você ver a mensagem:**
```
🔍 [CACHE MISS] Buscando tarefas no banco de dados...
```
**Significa:** Cache não encontrou, foi buscar no banco

**Se você NÃO ver a mensagem:**
**Significa:** Dados vieram do cache! ⚡

### Método 2: Comparar Tempos

**Primeira requisição:** ~150ms (vai ao banco)
**Segunda requisição:** ~5ms (vem do cache)

**Diferença:** ~30x mais rápido!

### Método 3: Testar Invalidação

1. Liste tarefas (armazena no cache)
2. Crie uma nova tarefa (invalida cache)
3. Liste tarefas novamente (deve buscar no banco)

**Se a nova tarefa aparecer:** Cache está funcionando! ✅

---

## 🔧 Endpoints de Gerenciamento de Cache

### Listar Caches
```
GET /cache
Authorization: Bearer {token_admin}
```

### Limpar Cache Específico
```
DELETE /cache/tarefas
Authorization: Bearer {token_admin}
```

### Limpar Todos os Caches
```
DELETE /cache/clear
Authorization: Bearer {token_admin}
```

---

## 💡 Dicas

1. **Logs são temporários**: Os `System.out.println` podem ser removidos depois de testar
2. **Cache expira automaticamente**: Após 10 minutos, cache expira mesmo sem invalidação
3. **Cache é em memória**: Se reiniciar a aplicação, cache é limpo
4. **Performance**: Quanto mais requisições, maior o benefício do cache

---

## ✅ Checklist de Teste

- [ ] Primeira requisição mostra log `[CACHE MISS]`
- [ ] Segunda requisição **NÃO** mostra log (cache hit)
- [ ] Criar tarefa mostra log `[CACHE EVICT]`
- [ ] Após criar, próxima leitura mostra log `[CACHE MISS]`
- [ ] Nova tarefa aparece na lista
- [ ] Tempo de resposta é muito menor na segunda requisição

---

**Agora teste e veja o cache em ação!** 🚀

Você verá a diferença de performance imediatamente nos logs e no tempo de resposta!

