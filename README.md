🌱 EcoTask — Plataforma de Tarefas Sustentáveis 
O EcoTask é um sistema completo para gerenciamento de tarefas e missões sustentáveis, com gamificação, segurança, paginação, cache, internacionalização, IA generativa e deploy em nuvem.

Ele foi desenvolvido utilizando Java + Spring Boot, seguindo as melhores práticas de arquitetura REST, segurança com JWT, persistência com Spring Data JPA e integração com IA usando Spring AI + LangChain4J.

🎥 Vídeo de apresentação do projeto:  
https://youtu.be/4tpEZfukbGY


📌 Índice
- Principais Funcionalidades
- Tecnologias Utilizadas
- Arquitetura do Projeto
- Configuração do Ambiente
- Banco de Dados (PostgreSQL)
- Autenticação e Segurança (JWT)
- Internacionalização (i18n)
- Caching
- Validações (Bean Validation)
- Paginação
- Tratamento de erros
- IA Ambiental (Spring AI)
- Coleção de Endpoints
- Como Rodar o Projeto
- Deploy em Nuvem
- Integrantes do Projeto


✨ Principais Funcionalidades
✔ CRUD completo para:
- Usuários
- Tarefas Sustentáveis
- Categorias
- Missões
- Recompensas

✔ Autenticação e autorização com Spring Security + JWT  
✔ Validações com Bean Validation  
✔ Paginação nativa com Spring Data  
✔ Internacionalização (i18n) — pt-BR e en-US  
✔ Cache para otimizar desempenho  
✔ Tratamento global de erros  
✔ IA generativa especializada em sustentabilidade  
✔ API REST seguindo boas práticas  
✔ Deploy em nuvem (Render)


🧪 Tecnologias Utilizadas
- Java 17  
- Spring Boot 3  
- Spring Data JPA  
- Spring Security + JWT  
- Spring Cache  
- Spring Validation  
- Spring AI  
- LangChain4J  
- PostgreSQL (Deploy em nuvem)  
- Maven  


🗂 Arquitetura do Projeto
src/main/java/com/example/demo  
 ├── controller  
 ├── service  
 │    ├── ia  
 ├── repository  
 ├── domain  
 │    ├── model  
 │    └── dto  
 ├── config  
 ├── exception  
 ├── security  
 └── EcoTaskApplication.java  


🛠 Configuração do Ambiente
Arquivo application.properties:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/ecotask
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```


🗄 Banco de Dados (PostgreSQL)
O projeto utiliza PostgreSQL.

Criação do banco local (caso necessário):

```
CREATE DATABASE ecotask;
```

💡 **Observação importante:**  
O projeto em produção utiliza **PostgreSQL completamente em nuvem**, configurado no deploy Render.  
Não é necessário acessar o banco diretamente — **todos os testes podem ser feitos pelos endpoints do deploy Java**, já funcionando com todos os dados e integrações.


🔐 Autenticação e Segurança (JWT)
A autenticação usa o prefixo:

```
/auth
```

Endpoints Públicos:
```
POST /auth/register
POST /auth/login
```

Após o login, você receberá um token JWT:

```
Authorization: Bearer SEU_TOKEN
```

Endpoints Protegidos:
✔ Token JWT válido  
✔ Role obrigatória: **ADMIN**  


🌎 Internacionalização (i18n)
Idiomas suportados:
- Português (pt-BR)
- Inglês (en-US)

Arquivos:
- messages.properties  
- messages_en.properties  

Para trocar o idioma no Postman:
```
Accept-Language: en-US
```


⚡ Caching
Usa as anotações:
```
@Cacheable
@CacheEvict
@Caching
```


✔ Validações (Bean Validation)
Anotações usadas:
```
@NotBlank
@Size
@Email
@NotNull
@Positive
```


📄 Paginação
Exemplo:
```
GET /tarefas?page=0&size=10
```


🚫 Tratamento Global de Erros
Exemplo de retorno:

```
{
  "status": 400,
  "message": "Campo inválido",
  "details": "O nome não pode ser vazio"
}
```


🤖 IA Ambiental (Spring AI)

Endpoint da IA:
```
POST /api/assistant
```

Exemplo de body:
```
{
  "message": "Me dê uma dica de como economizar água."
}
```

Serviço utilizado:

```java
@AiService
public interface AssistantAiService {

    @SystemMessage("""
        Você é uma inteligência artificial especializada em ajudar os usuários a entender e praticar ações que beneficiam o meio ambiente. 
        Fale de forma positiva e responda apenas temas ligados à sustentabilidade.
        Caso o usuário pergunte algo fora do tema, diga:
        'Desculpe — só posso responder perguntas sobre tarefas e atitudes que ajudam a natureza.'
        """)
    Result<String> handleRequest(@UserMessage String userMessage);
}
```


🔗 Coleção de Endpoints

🔐 Autenticação
```
POST /auth/register
POST /auth/login
```

👤 Usuários (ADMIN)
```
GET    /usuarios
GET    /usuarios/{id}
POST   /usuarios
PUT    /usuarios/{id}
DELETE /usuarios/{id}
```

📝 Tarefas
```
GET    /tarefas
POST   /tarefas
PUT    /tarefas/{id}
DELETE /tarefas/{id}
```

🏷 Categorias
```
GET    /categorias
POST   /categorias
PUT    /categororias/{id}
DELETE /categororias/{id}
```

🗺 Missões
```
GET    /missoes
POST   /missoes
PUT    /missoes/{id}
DELETE /missoes/{id}
```

🎁 Recompensas
```
GET    /recompensas
POST   /recompensas
PUT    /recompensas/{id}
DELETE /recompensas/{id}
```

🤖 IA Ambiental
```
POST /api/assistant
```


▶ Como Rodar o Projeto Localmente

1. Clonar o repositório
```
git clone https://github.com/felipegresele/ecotask-java
```

2. Criar o banco local (opcional)
```
CREATE DATABASE ecotask;
```

3. Rodar o projeto
```
mvn spring-boot:run
```


☁ Deploy em Nuvem (Render)
Backend Java:  
https://ecotask-java.onrender.com  

💡 Observação  
O deploy já possui:
- Banco PostgreSQL configurado  
- IA ativa  
- Autenticação funcionando  
- Todos os endpoints habilitados  

Para testar, basta usar o deploy + token ADMIN criado via `/auth/register`.


👨‍💻 Integrantes do Projeto
Nome                         | RM  
---------------------------- | --------  
Felipe Horta Gresele        | RM556955  
Arthur Cardoso Carinhanha   | RM550615  
João Henrique Dias          | RM556221  
