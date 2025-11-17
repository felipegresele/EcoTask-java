# 🌱 EcoTask — Plataforma de Tarefas Sustentáveis

O **EcoTask** é um sistema completo para gerenciamento de tarefas e missões sustentáveis, com gamificação, segurança, paginação, cache, internacionalização, IA generativa e deploy em nuvem.

Ele foi desenvolvido utilizando **Java + Spring Boot**, seguindo as melhores práticas de arquitetura REST, segurança com JWT, persistência com Spring Data JPA e integração com IA usando **Spring AI + LangChain4J**.

---

# 📌 Índice

1. [Principais Funcionalidades](#-principais-funcionalidades)
2. [Tecnologias Utilizadas](#-tecnologias-utilizadas)
3. [Arquitetura do Projeto](#-arquitetura-do-projeto)
4. [Configuração do Ambiente](#-configuração-do-ambiente)
5. [Banco de Dados (PostgreSQL)](#-banco-de-dados-postgresql)
6. [Autenticação e Segurança (JWT)](#-autenticação-e-segurança-jwt)
7. [Internacionalização (i18n)](#-internacionalização-i18n)
8. [Caching](#-caching)
9. [Validações (Bean Validation)](#-validações-bean-validation)
10. [Paginação](#-paginação)
11. [Tratamento de erros](#-tratamento-de-erros)
12. [IA Ambiental (Spring AI)](#-ia-ambiental-spring-ai)
13. [Coleção de Endpoints](#-coleção-de-endpoints)
14. [Como Rodar o Projeto](#-como-rodar-o-projeto)
15. [Deploy em Nuvem](#-deploy-em-nuvem)
16. [Integrantes do Projeto](#-integrantes-do-projeto)

---

# ✨ Principais Funcionalidades

✔ CRUD completo para:
- Usuários
- Tarefas Sustentáveis
- Categorias
- Missões
- Recompensas

✔ Autenticação e autorização com **Spring Security + JWT**  
✔ Validações com **Bean Validation**  
✔ Paginação nativa com Spring Data  
✔ Internacionalização (i18n) — **pt-BR** e **en-US**  
✔ Cache para otimizar desempenho  
✔ Tratamento global de erros  
✔ IA generativa especializada em sustentabilidade  
✔ API REST seguindo boas práticas  
✔ Deploy em nuvem (Render)

---

# 🧪 Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Security + JWT**
- **Spring Cache**
- **Spring Validation**
- **Spring AI**
- **LangChain4J**
- **PostgreSQL**
- **Maven**

---

# 🗂 Arquitetura do Projeto

src/main/java/com/example/demo
├── controller
├── service
│ ├── ia
├── repository
├── domain
│ ├── model
│ └── dto
├── config
├── exception
├── security
└── EcoTaskApplication.java

yaml
Copiar código

---

# 🛠 Configuração do Ambiente

Arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecotask
spring.datasource.username=postgres
spring.datasource.password=senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
🗄 Banco de Dados (PostgreSQL)
O projeto utiliza um banco PostgreSQL.

Criação do banco:

sql
Copiar código
CREATE DATABASE ecotask;
🔐 Autenticação e Segurança (JWT)
A autenticação usa o prefixo:

bash
Copiar código
/auth
Endpoints Públicos
bash
Copiar código
POST /auth/register
POST /auth/login
Após o login, você receberá um token JWT:

makefile
Copiar código
Authorization: Bearer SEU_TOKEN
Endpoints Protegidos
Todos os demais endpoints exigem:

✔ Token JWT válido
✔ Role: ADMIN

🌎 Internacionalização (i18n)
O projeto suporta duas línguas:

Português (padrão)

Inglês (en-US)

Arquivos:

matlab
Copiar código
messages.properties
messages_en.properties
Para trocar o idioma no Postman:

makefile
Copiar código
Accept-Language: en-US
⚡ Caching
Implementado com:

java
Copiar código
@Cacheable
@CacheEvict
@Caching
Usado para melhorar performance em endpoints com muitas leituras.

✔ Validações (Bean Validation)
Exemplos usados:

java
Copiar código
@NotBlank
@Size
@Email
@NotNull
@Positive
Garantem integridade dos dados enviados ao sistema.

📄 Paginação
Padrão do Spring Boot:

arduino
Copiar código
GET /tarefas?page=0&size=10
🚫 Tratamento Global de Erros
Retorno de erro estruturado:

json
Copiar código
{
  "status": 400,
  "message": "Campo inválido",
  "details": "O nome não pode ser vazio"
}
🤖 IA Ambiental (Spring AI)
Endpoint da IA
bash
Copiar código
POST /api/assistant
Como usar no Postman
Body → JSON:

json
Copiar código

  "message": "Me dê uma dica de como economizar água."

A IA responderá automaticamente usando o modelo LangChain4J.

Serviço utilizado:
java
Copiar código
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
🔗 Coleção de Endpoints
🔐 Autenticação
bash
Copiar código
POST /auth/register
POST /auth/login
👤 Usuários (ADMIN)
bash
Copiar código
GET    /usuarios
GET    /usuarios/{id}
POST   /usuarios
PUT    /usuarios/{id}
DELETE /usuarios/{id}
📝 Tarefas
bash
Copiar código
GET    /tarefas
POST   /tarefas
PUT    /tarefas/{id}
DELETE /tarefas/{id}
🏷 Categorias
bash
Copiar código
GET    /categorias
POST   /categorias
PUT    /categorias/{id}
DELETE /categorias/{id}
🗺 Missões
bash
Copiar código
GET    /missoes
POST   /missoes
PUT    /missoes/{id}
DELETE /missoes/{id}
🎁 Recompensas
bash
Copiar código
GET    /recompensas
POST   /recompensas
PUT    /recompensas/{id}
DELETE /recompensas/{id}
🤖 IA Ambiental
bash
Copiar código
POST /api/assistant
▶ Como Rodar o Projeto
1. Clonar o repositório
bash
Copiar código
git clone https://github.com/felipegresele/ecotask-java
2. Criar o banco
sql
Copiar código
CREATE DATABASE ecotask;
3. Rodar o projeto
bash
Copiar código
mvn spring-boot:run
☁ Deploy em Nuvem (Render)
Configurar variáveis de ambiente (DB, JWT_SECRET, etc.)

Subir como serviço Web Java 21

👨‍💻 Integrantes do Projeto
Nome	RM
Felipe Horta Gresele	RM556955
Arthur Cardoso Carinhanha	RM550615
João Henrique Dias	RM556221