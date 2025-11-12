# 🌍 Internacionalização (i18n) e Paginação

## 📋 Índice
1. [Internacionalização (i18n)](#internacionalização-i18n)
2. [Paginação](#paginação)
3. [Como Usar](#como-usar)
4. [Exemplos de Requisições](#exemplos-de-requisições)

---

## 🌍 Internacionalização (i18n)

### Suporte a Idiomas
A aplicação suporta **dois idiomas**:
- **Português (Brasil)** - `pt-BR` (padrão)
- **Inglês (Estados Unidos)** - `en-US`

### Como Funciona

A aplicação detecta automaticamente o idioma do cliente através do header HTTP `Accept-Language`.

**Exemplo:**
```
Accept-Language: pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7
```

Se o header não for fornecido, o idioma padrão (Português) será usado.

### Arquivos de Mensagens

As mensagens estão localizadas em:
- `src/main/resources/messages_pt_BR.properties` - Mensagens em Português
- `src/main/resources/messages_en_US.properties` - Mensagens em Inglês

### Mensagens Internacionalizadas

Todas as mensagens de erro e validação são internacionalizadas:

#### Validação
- `validation.error.title` - Título de erro de validação
- `validation.params.error.title` - Título de erro de validação de parâmetros

#### Recursos
- `resource.not.found` - Recurso não encontrado
- `resource.task.not.found` - Tarefa não encontrada
- `resource.category.not.found` - Categoria não encontrada
- `resource.user.not.found` - Usuário não encontrado

#### Autenticação
- `auth.invalid.credentials` - Credenciais inválidas
- `auth.error` - Erro de autenticação
- `auth.email.already.registered` - Email já cadastrado
- `auth.user.registered` - Usuário cadastrado com sucesso

#### Paginação
- `pagination.invalid.page` - Número de página inválido
- `pagination.invalid.size` - Tamanho da página inválido

#### Erros Gerais
- `error.internal.server` - Erro interno do servidor
- `error.processing.request` - Erro ao processar requisição

---

## 📄 Paginação

### Endpoint de Paginação

**GET** `/tarefas/paginated`

### Parâmetros de Query

| Parâmetro | Tipo | Obrigatório | Padrão | Descrição |
|-----------|------|-------------|--------|-----------|
| `page` | int | Não | 0 | Número da página (0-indexed) |
| `size` | int | Não | 10 | Tamanho da página (1-100) |

### Resposta Paginada

```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Tarefa 1",
      "descricao": "Descrição da tarefa",
      "completado": false,
      "dataCriacao": "2024-01-15",
      "points": 10,
      "categoria": { ... },
      "usuario": { ... }
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

### Campos da Resposta

| Campo | Tipo | Descrição |
|-------|------|-----------|
| `content` | Array | Lista de tarefas na página atual |
| `page` | int | Número da página atual (0-indexed) |
| `size` | int | Tamanho da página |
| `totalElements` | long | Total de elementos em todas as páginas |
| `totalPages` | int | Total de páginas |
| `first` | boolean | Indica se é a primeira página |
| `last` | boolean | Indica se é a última página |

### Validações

- **page**: Deve ser >= 0
- **size**: Deve estar entre 1 e 100

Se os parâmetros forem inválidos, uma mensagem de erro internacionalizada será retornada.

---

## 🚀 Como Usar

### 1. Testando Internacionalização

#### Requisição em Português (Padrão)
```bash
GET /tarefas/paginated?page=0&size=10
Accept-Language: pt-BR
```

#### Requisição em Inglês
```bash
GET /tarefas/paginated?page=0&size=10
Accept-Language: en-US
```

### 2. Testando Paginação

#### Primeira Página (10 itens)
```bash
GET /tarefas/paginated?page=0&size=10
```

#### Segunda Página (10 itens)
```bash
GET /tarefas/paginated?page=1&size=10
```

#### Página com 20 itens
```bash
GET /tarefas/paginated?page=0&size=20
```

---

## 📝 Exemplos de Requisições

### Exemplo 1: Listar Tarefas Paginadas (Português)

**Requisição:**
```http
GET /tarefas/paginated?page=0&size=10
Accept-Language: pt-BR
Authorization: Bearer {token}
```

**Resposta:**
```json
{
  "content": [
    {
      "id": 1,
      "titulo": "Reciclar papel",
      "descricao": "Separar e reciclar papel",
      "completado": false,
      "dataCriacao": "2024-01-15",
      "points": 10
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false
}
```

### Exemplo 2: Erro de Validação (Inglês)

**Requisição:**
```http
GET /tarefas/paginated?page=-1&size=10
Accept-Language: en-US
Authorization: Bearer {token}
```

**Resposta:**
```json
{
  "status": 400,
  "message": "Invalid page number",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Exemplo 3: Erro de Validação (Português)

**Requisição:**
```http
GET /tarefas/paginated?page=0&size=200
Accept-Language: pt-BR
Authorization: Bearer {token}
```

**Resposta:**
```json
{
  "status": 400,
  "message": "Tamanho da página inválido",
  "timestamp": "2024-01-15T10:30:00"
}
```

### Exemplo 4: Tarefa Não Encontrada (Inglês)

**Requisição:**
```http
GET /tarefas/999
Accept-Language: en-US
Authorization: Bearer {token}
```

**Resposta:**
```json
{
  "status": 404,
  "message": "Task not found with ID: 999",
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 🔧 Configuração

### application.properties

```properties
# Configuração de Internacionalização (i18n)
spring.web.locale=pt_BR
spring.web.locale-resolver=accept_header
```

### MessageSourceConfig

A configuração do `MessageSource` e `LocaleResolver` está em:
`src/main/java/com/example/demo/infra/config/MessageSourceConfig.java`

### WebMvcConfig

A configuração do interceptor de locale está em:
`src/main/java/com/example/demo/infra/config/WebMvcConfig.java`

---

## 🎯 Cache e Paginação

A paginação está integrada com o sistema de cache:

- Cada página é cacheada separadamente
- Chave do cache: `page:{pageNumber}:size:{pageSize}`
- Quando uma tarefa é criada, atualizada ou deletada, todas as páginas são invalidadas

**Exemplo de chaves de cache:**
- `page:0:size:10` - Primeira página com 10 itens
- `page:1:size:10` - Segunda página com 10 itens
- `page:0:size:20` - Primeira página com 20 itens

---

## 📌 Notas Importantes

1. **Idioma Padrão**: Se o header `Accept-Language` não for fornecido, o idioma padrão (Português) será usado.

2. **Fallback**: Se um idioma não suportado for solicitado, o idioma padrão será usado.

3. **Paginação**: O endpoint `/tarefas` (sem paginação) ainda está disponível para compatibilidade.

4. **Tamanho Máximo**: O tamanho máximo da página é 100 itens para evitar sobrecarga.

5. **Cache**: O cache de paginação é invalidado automaticamente quando tarefas são criadas, atualizadas ou deletadas.

---

## 🧪 Testando no Postman

### 1. Configurar Header Accept-Language

No Postman, adicione o header:
```
Accept-Language: pt-BR
```
ou
```
Accept-Language: en-US
```

### 2. Testar Paginação

1. Faça uma requisição GET para `/tarefas/paginated?page=0&size=10`
2. Verifique a resposta paginada
3. Faça outra requisição para `/tarefas/paginated?page=1&size=10`
4. Compare os resultados

### 3. Testar Mensagens de Erro

1. Faça uma requisição com `page=-1` (inválido)
2. Verifique a mensagem de erro no idioma configurado
3. Mude o header `Accept-Language` e faça a mesma requisição
4. Verifique que a mensagem está no novo idioma

---

## ✅ Checklist de Implementação

- [x] Arquivos de mensagens (pt-BR e en-US)
- [x] Configuração do MessageSource
- [x] Configuração do LocaleResolver
- [x] Atualização do ValidationExceptionHandler
- [x] Atualização do AuthenticationController
- [x] DTO de resposta paginada (PageResponse)
- [x] Service de paginação (TarefaService)
- [x] Controller de paginação (TarefaController)
- [x] Integração com cache
- [x] Validação de parâmetros de paginação
- [x] Mensagens de erro internacionalizadas

---

## 🎉 Conclusão

A aplicação agora suporta:
- ✅ **Internacionalização** com português e inglês
- ✅ **Paginação** no endpoint de tarefas
- ✅ **Cache** integrado com paginação
- ✅ **Mensagens de erro** internacionalizadas
- ✅ **Validação** de parâmetros de paginação

**Tudo pronto para uso!** 🚀

