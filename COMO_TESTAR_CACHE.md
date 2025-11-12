# 🧪 Como Testar o Cache

## 📋 Pré-requisitos

1. Aplicação rodando
2. Postman ou similar
3. Token JWT de um usuário ADMIN

## 🔍 Teste 1: Verificar Cache em Lista de Tarefas

### Passo 1: Fazer Login
```http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "email": "admin@example.com",
  "password": "admin123"
}
```

**Copie o token da resposta!**

### Passo 2: Primeira Requisição (Cache Miss)
```http
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

**O que acontece:**
- ✅ Spring verifica o cache → **Não encontra** (cache miss)
- ✅ Executa `listarTodasTarefas()`
- ✅ Busca no banco de dados
- ✅ Armazena resultado no cache
- ✅ Retorna resposta

**Tempo estimado:** ~50-200ms (dependendo do banco)

### Passo 3: Segunda Requisição (Cache Hit)
```http
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

**O que acontece:**
- ✅ Spring verifica o cache → **Encontra!** (cache hit)
- ✅ Retorna direto do cache (sem acessar banco)
- ✅ Resposta muito mais rápida

**Tempo estimado:** ~1-5ms (memória)

### Como Verificar se Está Funcionando

**Opção 1: Comparar Tempo de Resposta**
- Primeira requisição: Mais lenta (vai ao banco)
- Segunda requisição: Muito mais rápida (vem do cache)

**Opção 2: Ver Logs da Aplicação**
Se você habilitar logs de cache no `application.properties`:
```properties
logging.level.org.springframework.cache=DEBUG
```

Você verá mensagens como:
```
Cache 'tarefas' hit for key 'all'
Cache 'tarefas' miss for key 'all'
```

**Opção 3: Adicionar Log no Service**
Adicione um `System.out.println` temporário no service para ver quando o método é executado:

```java
@Cacheable(value = "tarefas", key = "'all'")
public List<Tarefa> listarTodasTarefas() {
    System.out.println(">>> BUSCANDO NO BANCO DE DADOS <<<");
    return tarefaRepository.findAll();
}
```

**Comportamento esperado:**
- Primeira chamada: Vê a mensagem ">>> BUSCANDO NO BANCO DE DADOS <<<"
- Segunda chamada: **NÃO** vê a mensagem (veio do cache)

---

## 🔍 Teste 2: Verificar Invalidação de Cache

### Passo 1: Listar Tarefas (armazena no cache)
```http
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

### Passo 2: Criar Nova Tarefa (invalida cache)
```http
POST http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "titulo": "Nova tarefa de teste",
  "descricao": "Testando invalidação de cache",
  "completado": false,
  "dataCriacao": "2024-01-15",
  "points": 10,
  "categoriaId": 1,
  "usuarioId": 1
}
```

**O que acontece:**
- ✅ Cria a tarefa no banco
- ✅ `@CacheEvict` remove a lista do cache
- ✅ Cache da lista é limpo

### Passo 3: Listar Tarefas Novamente
```http
GET http://localhost:8080/tarefas
Authorization: Bearer {seu_token}
```

**O que acontece:**
- ✅ Cache foi invalidado
- ✅ Spring verifica o cache → **Não encontra** (cache miss)
- ✅ Busca no banco novamente (incluindo a nova tarefa)
- ✅ Armazena nova lista no cache
- ✅ Retorna resposta com a nova tarefa

**Resultado:** A nova tarefa aparece na lista!

---

## 🔍 Teste 3: Cache de Tarefa Individual

### Passo 1: Buscar Tarefa por ID (primeira vez)
```http
GET http://localhost:8080/tarefas/1
Authorization: Bearer {seu_token}
```

**Cache miss** → Busca no banco → Armazena no cache

### Passo 2: Buscar Mesma Tarefa (segunda vez)
```http
GET http://localhost:8080/tarefas/1
Authorization: Bearer {seu_token}
```

**Cache hit** → Retorna do cache (muito rápido!)

### Passo 3: Atualizar Tarefa
```http
PUT http://localhost:8080/tarefas/1
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "titulo": "Tarefa atualizada",
  "descricao": "Descrição atualizada",
  "completado": true,
  "dataCriacao": "2024-01-15",
  "points": 20,
  "categoriaId": 1,
  "usuarioId": 1
}
```

**O que acontece:**
- ✅ Atualiza no banco
- ✅ `@Caching` remove cache individual (ID 1) E lista
- ✅ Cache é limpo

### Passo 4: Buscar Tarefa Novamente
```http
GET http://localhost:8080/tarefas/1
Authorization: Bearer {seu_token}
```

**Cache miss** → Busca no banco → Retorna dados atualizados

---

## 🔍 Teste 4: Gerenciar Cache Manualmente

### Listar Caches Disponíveis
```http
GET http://localhost:8080/cache
Authorization: Bearer {seu_token_admin}
```

**Resposta:**
```json
{
  "caches": ["tarefas", "tarefa", "categorias", "categoria", "usuarios"],
  "total": 5
}
```

### Limpar Cache Específico
```http
DELETE http://localhost:8080/cache/tarefas
Authorization: Bearer {seu_token_admin}
```

**Resposta:**
```json
{
  "message": "Cache 'tarefas' foi limpo com sucesso"
}
```

### Limpar Todos os Caches
```http
DELETE http://localhost:8080/cache/clear
Authorization: Bearer {seu_token_admin}
```

**Resposta:**
```json
{
  "message": "Todos os caches foram limpos com sucesso"
}
```

---

## 📊 Como Verificar se Cache Está Funcionando

### Método 1: Adicionar Log no Service (Temporário)

Adicione um log no método que você quer testar:

```java
@Cacheable(value = "tarefas", key = "'all'")
public List<Tarefa> listarTodasTarefas() {
    System.out.println("🔍 EXECUTANDO: listarTodasTarefas() - Buscando no banco");
    return tarefaRepository.findAll();
}
```

**Teste:**
1. Chame `GET /tarefas` → Verá o log
2. Chame `GET /tarefas` novamente → **NÃO** verá o log (veio do cache)
3. Crie uma nova tarefa → Cache é invalidado
4. Chame `GET /tarefas` novamente → Verá o log (busca no banco)

### Método 2: Comparar Tempos de Resposta

**No Postman:**
1. Primeira requisição: Veja o tempo de resposta (ex: 150ms)
2. Segunda requisição: Tempo muito menor (ex: 5ms)
3. Diferença: Cache está funcionando!

### Método 3: Habilitar Logs de Cache

No `application.properties`:
```properties
logging.level.org.springframework.cache=DEBUG
```

**Logs que você verá:**
```
Cache 'tarefas' hit for key 'all'
Cache 'tarefas' miss for key 'all'
Cache 'tarefas' evicted for key 'all'
```

---

## 🎯 Teste Completo: Fluxo Completo de Cache

### 1. Primeira Leitura (Cache Miss)
```bash
GET /tarefas
→ Cache: MISS
→ Banco: SELECT * FROM tarefa
→ Tempo: ~150ms
→ Armazena no cache
```

### 2. Segunda Leitura (Cache Hit)
```bash
GET /tarefas
→ Cache: HIT
→ Banco: (não acessa)
→ Tempo: ~5ms
→ Retorna do cache
```

### 3. Criar Nova Tarefa (Invalidar Cache)
```bash
POST /tarefas
→ Salva no banco
→ Cache: EVICT (remove lista)
→ Tempo: ~100ms
```

### 4. Terceira Leitura (Cache Miss - Dados Novos)
```bash
GET /tarefas
→ Cache: MISS (foi invalidado)
→ Banco: SELECT * FROM tarefa
→ Tempo: ~150ms
→ Armazena nova lista no cache (com nova tarefa)
```

### 5. Quarta Leitura (Cache Hit - Dados Atualizados)
```bash
GET /tarefas
→ Cache: HIT
→ Banco: (não acessa)
→ Tempo: ~5ms
→ Retorna do cache (com nova tarefa)
```

---

## 🔧 Adicionar Log Temporário para Teste

Vou criar um método auxiliar para você testar:

### Adicionar no TarefaService:

```java
@Cacheable(value = "tarefas", key = "'all'")
public List<Tarefa> listarTodasTarefas() {
    System.out.println("🔍 [CACHE MISS] Buscando tarefas no banco de dados...");
    List<Tarefa> tarefas = tarefaRepository.findAll();
    System.out.println("✅ [CACHE MISS] Encontradas " + tarefas.size() + " tarefas");
    return tarefas;
}
```

**Comportamento esperado:**
- Primeira chamada: Vê a mensagem
- Segunda chamada: **NÃO** vê a mensagem (cache hit)
- Após criar/atualizar: Vê a mensagem novamente (cache invalidado)

---

## 📝 Checklist de Teste

- [ ] Primeira requisição GET /tarefas (cache miss)
- [ ] Segunda requisição GET /tarefas (cache hit - mais rápido)
- [ ] Criar nova tarefa (invalida cache)
- [ ] Requisição GET /tarefas após criação (cache miss - busca nova tarefa)
- [ ] Buscar tarefa por ID (cache individual)
- [ ] Atualizar tarefa (invalida cache)
- [ ] Listar caches disponíveis
- [ ] Limpar cache manualmente
- [ ] Verificar que após limpar, próxima leitura vai ao banco

---

## 🚀 Dicas de Teste

1. **Use Postman Collection**: Crie uma collection com todas as requisições
2. **Compare tempos**: Veja a diferença de tempo entre cache hit e miss
3. **Teste invalidação**: Crie, atualize e delete para ver cache sendo invalidado
4. **Verifique dados**: Sempre verifique que os dados estão atualizados após invalidação
5. **Teste em produção**: Cache funciona melhor com mais requisições

---

## ⚠️ O Que Mudou no Código

### Antes (SEM Cache):
```java
public List<Tarefa> listarTodasTarefas() {
    return tarefaRepository.findAll(); // Sempre vai ao banco
}
```

### Depois (COM Cache):
```java
@Cacheable(value = "tarefas", key = "'all'")
public List<Tarefa> listarTodasTarefas() {
    return tarefaRepository.findAll(); // Vai ao banco apenas na primeira vez
}
```

**Diferença:**
- ✅ Primeira chamada: Vai ao banco
- ✅ Próximas chamadas: Retorna do cache (muito mais rápido)
- ✅ Após criar/atualizar: Cache é invalidado, próxima chamada vai ao banco

---

## 🎉 Resultado Esperado

Após implementar o cache:
- ✅ **Performance melhorada**: Respostas muito mais rápidas
- ✅ **Menos carga no banco**: Redução de ~80-90% nas consultas
- ✅ **Melhor escalabilidade**: Suporta mais requisições simultâneas
- ✅ **Dados sempre atualizados**: Cache é invalidado quando dados mudam

Teste e veja a diferença! 🚀

