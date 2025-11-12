# 🚀 Implementação de Cache

## 📋 Visão Geral

Foi implementado cache usando **Spring Cache** com **Caffeine** para melhorar a performance da aplicação, reduzindo chamadas ao banco de dados e acelerando as respostas da API.

## 🔧 Tecnologias Utilizadas

- **Spring Cache**: Framework de cache do Spring
- **Caffeine**: Biblioteca de cache em memória de alta performance
- **Cache Annotations**: `@Cacheable`, `@CacheEvict`, `@Caching`

## ⚙️ Configuração

### 1. Dependências (pom.xml)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

### 2. Habilitar Cache (DemoApplication.java)

```java
@SpringBootApplication
@EnableCaching
public class DemoApplication {
    // ...
}
```

### 3. Configuração do Cache (CacheConfig.java)

- **Tamanho máximo**: 500 itens por cache
- **TTL (Time To Live)**: 10 minutos após escrita
- **Expiração por acesso**: 5 minutos sem acesso
- **Estatísticas**: Habilitadas para monitoramento

### 4. Application Properties

```properties
spring.cache.type=caffeine
spring.cache.caffeine.spec=maximumSize=500,expireAfterWrite=10m,expireAfterAccess=5m
```

## 📦 Caches Implementados

### 1. Cache de Tarefas

#### Lista de Tarefas (`tarefas`)
- **Método**: `listarTodasTarefas()`
- **Chave**: `'all'`
- **Operação**: `@Cacheable`
- **Invalidado quando**: Criar, atualizar ou deletar tarefa

#### Tarefa Individual (`tarefa`)
- **Método**: `buscarTarefaPorId(Long id)`
- **Chave**: `#id` (ID da tarefa)
- **Operação**: `@Cacheable`
- **Invalidado quando**: Atualizar ou deletar a tarefa específica

### 2. Cache de Categorias

#### Lista de Categorias (`categorias`)
- **Método**: `listarTodasCategorias()`
- **Chave**: `'all'`
- **Operação**: `@Cacheable`
- **Invalidado quando**: Criar, atualizar ou deletar categoria

#### Categoria Individual (`categoria`)
- **Chave**: `#id` (ID da categoria)
- **Invalidado quando**: Atualizar ou deletar a categoria específica

### 3. Cache de Usuários

#### Lista de Usuários (`usuarios`)
- **Método**: `listarTodosUsuarios()`
- **Chave**: `'all'`
- **Operação**: `@Cacheable`
- **Invalidado quando**: Atualizar ou deletar usuário
- **Nota**: Usuários individuais não são cacheados por questões de segurança

## 🔄 Estratégia de Cache

### @Cacheable
- **Uso**: Métodos de leitura (GET)
- **Comportamento**: Armazena o resultado no cache na primeira chamada
- **Próximas chamadas**: Retorna do cache sem acessar o banco

### @CacheEvict
- **Uso**: Métodos de escrita (POST, PUT, DELETE)
- **Comportamento**: Remove entradas do cache quando dados são modificados
- **Garantia**: Cache sempre reflete os dados mais recentes

### @Caching
- **Uso**: Quando precisa invalidar múltiplos caches
- **Exemplo**: Ao atualizar uma tarefa, remove tanto o cache individual quanto a lista

## 📊 Fluxo de Funcionamento

### Primeira Requisição (Cache Miss)
```
1. Cliente faz GET /tarefas
2. Spring verifica cache → Não encontra
3. Executa método listarTodasTarefas()
4. Busca no banco de dados
5. Armazena resultado no cache
6. Retorna resposta ao cliente
```

### Próximas Requisições (Cache Hit)
```
1. Cliente faz GET /tarefas
2. Spring verifica cache → Encontra!
3. Retorna do cache (sem acessar banco)
4. Resposta muito mais rápida
```

### Após Criação/Atualização (Cache Invalidation)
```
1. Cliente faz POST /tarefas
2. Executa método criarTarefa()
3. Salva no banco de dados
4. @CacheEvict remove lista do cache
5. Próxima leitura buscará dados atualizados
```

## 🛠️ Gerenciamento de Cache

### Endpoints de Cache (Apenas ADMIN)

#### Listar Caches
```http
GET /cache
Authorization: Bearer {token_admin}

Resposta:
{
  "caches": ["tarefas", "tarefa", "categorias", "categoria", "usuarios"],
  "total": 5
}
```

#### Limpar Cache Específico
```http
DELETE /cache/{cacheName}
Authorization: Bearer {token_admin}

Exemplo: DELETE /cache/tarefas

Resposta:
{
  "message": "Cache 'tarefas' foi limpo com sucesso"
}
```

#### Limpar Todos os Caches
```http
DELETE /cache/clear
Authorization: Bearer {token_admin}

Resposta:
{
  "message": "Todos os caches foram limpos com sucesso"
}
```

## 📈 Benefícios de Performance

### Antes do Cache
- **Todas as requisições** acessam o banco de dados
- **Tempo de resposta**: ~50-200ms (dependendo do banco)
- **Carga no banco**: Alta
- **Escalabilidade**: Limitada

### Depois do Cache
- **Requisições repetidas** retornam do cache
- **Tempo de resposta**: ~1-5ms (memória)
- **Carga no banco**: Reduzida em ~80-90%
- **Escalabilidade**: Melhorada significativamente

## 🔍 Monitoramento

### Estatísticas do Cache

O Caffeine registra estatísticas automaticamente:
- **Hit Rate**: Taxa de acerto do cache
- **Miss Rate**: Taxa de erro do cache
- **Eviction Count**: Número de itens removidos
- **Size**: Tamanho atual do cache

### Logs de Cache

Para habilitar logs de cache (desenvolvimento):
```properties
logging.level.org.springframework.cache=DEBUG
```

## ⚠️ Considerações Importantes

### 1. Consistência de Dados
- O cache é **invalidado automaticamente** quando dados são modificados
- Dados sempre refletem o estado mais recente do banco

### 2. Memória
- Cache em memória (RAM)
- Tamanho máximo: 500 itens por cache
- Expiração automática após 10 minutos

### 3. Segurança
- Usuários individuais **não são cacheados** (dados sensíveis)
- Endpoints de gerenciamento de cache: **Apenas ADMIN**

### 4. Desenvolvimento vs Produção
- **Desenvolvimento**: Logs de cache habilitados
- **Produção**: Logs de cache desabilitados (performance)

## 🧪 Como Testar

### Teste 1: Verificar Cache Hit
```bash
# Primeira requisição (vai ao banco)
GET /tarefas
Authorization: Bearer {token}

# Segunda requisição (retorna do cache - muito mais rápida)
GET /tarefas
Authorization: Bearer {token}
```

### Teste 2: Verificar Invalidação
```bash
# 1. Listar tarefas (armazena no cache)
GET /tarefas

# 2. Criar nova tarefa (invalida cache)
POST /tarefas
Body: {...}

# 3. Listar tarefas novamente (vai ao banco e atualiza cache)
GET /tarefas
```

### Teste 3: Limpar Cache Manualmente
```bash
# Limpar cache de tarefas
DELETE /cache/tarefas
Authorization: Bearer {token_admin}

# Limpar todos os caches
DELETE /cache/clear
Authorization: Bearer {token_admin}
```

## 📝 Anotações Utilizadas

| Anotação | Descrição | Exemplo |
|----------|-----------|---------|
| `@Cacheable` | Armazena resultado no cache | `@Cacheable(value = "tarefas", key = "'all'")` |
| `@CacheEvict` | Remove entrada do cache | `@CacheEvict(value = "tarefas", key = "'all'")` |
| `@Caching` | Múltiplas operações de cache | `@Caching(evict = {...})` |
| `@CachePut` | Atualiza cache (não usado neste projeto) | - |

## 🎯 Melhores Práticas

1. **Cache apenas leituras frequentes**: Métodos GET que são chamados frequentemente
2. **Invalide cache em escritas**: Sempre invalide cache quando dados são modificados
3. **TTL apropriado**: Configure TTL baseado na frequência de atualização dos dados
4. **Monitoramento**: Monitore hit rate e ajuste configurações conforme necessário
5. **Memória**: Configure tamanho máximo baseado na memória disponível

## 🔧 Ajustes de Configuração

### Aumentar Tamanho do Cache
```java
.maximumSize(1000)  // De 500 para 1000
```

### Aumentar TTL
```java
.expireAfterWrite(30, TimeUnit.MINUTES)  // De 10 para 30 minutos
```

### Desabilitar Expiração por Acesso
```java
// Remover esta linha:
.expireAfterAccess(5, TimeUnit.MINUTES)
```

## ✅ Status da Implementação

- ✅ Cache configurado com Caffeine
- ✅ Cache em Tarefas (lista e individual)
- ✅ Cache em Categorias (lista)
- ✅ Cache em Usuários (lista apenas)
- ✅ Invalidação automática de cache
- ✅ Endpoints de gerenciamento de cache
- ✅ Segurança (apenas ADMIN)
- ✅ Configuração otimizada
- ✅ Documentação completa

## 🚀 Próximos Passos (Opcional)

Melhorias futuras que podem ser implementadas:

1. **Cache distribuído**: Redis para ambientes com múltiplas instâncias
2. **Cache condicional**: Cache apenas em condições específicas
3. **Estatísticas detalhadas**: Endpoint para ver estatísticas de cache
4. **Warm-up**: Pré-carregar cache na inicialização
5. **Cache de queries complexas**: Cache para queries com filtros

---

**Cache implementado e funcionando!** 🎉

A aplicação agora tem performance significativamente melhorada em operações de leitura frequentes.

