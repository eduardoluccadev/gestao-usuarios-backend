# 🚀 Desafio Técnico – Sistema de Gestão de Usuários

## 📌 Objetivo
Construir um sistema de **gestão de usuários** com autenticação e CRUD, seguindo boas práticas de desenvolvimento, versionamento e documentação.  
Este backend foi desenvolvido em **Java 21.0.5 + Spring Boot**, com **JWT** para autenticação, **Flyway** para migrações automáticas e **SQL Server** como banco de dados.

---

## 🧩 Funcionalidades Implementadas

✅ **Tela de Cadastro (Sign Up)**  
✅ **Tela de Login** com fluxo de **“Esqueci minha senha”**  
✅ **Área logada** com autenticação JWT  
✅ **Listagem de usuários cadastrados**  
✅ **Criação de novo usuário**  
✅ **Edição de usuário existente**  
✅ **Remoção de usuários**  
✅ **Logout**  
✅ **Migrações automáticas** via Flyway  
✅ **Documentação da API com Swagger/OpenAPI**

---

## ⚙️ Como instalar

### 🔧 Pré-requisitos
- ☕ **Java 21.0.5**  
- 🧰 **Maven**  
- 🗄️ **SQL Server** (instalado ou em container)  
- 🌀 **Git**

---

### 🗂️ Passos de configuração

#### 1️⃣ Clonar o projeto
```bash
git clone https://github.com/eduardoluccadev/gestao-usuarios-backend.git
cd gestao-usuarios-backend
CREATE DATABASE gestao_usuarios;

3️⃣ Configurar o ambiente

Crie o arquivo src/main/resources/application.yml:
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=gestao_usuarios;encrypt=false
    username: sa
    password: SUA_SENHA
  jpa:
    hibernate:
      ddl-auto: none
    show-sql: true
  flyway:
    enabled: true
    locations: classpath:db/migration

security:
  jwt:
    secret: SUA_CHAVE_SECRETA
    expirationMinutes: 60

4️⃣ Executar o projeto
./mvnw clean install
./mvnw spring-boot:run
🔗 Acesse a API: http://localhost:8080

5️⃣ Documentação Swagger
Após rodar o projeto:
👉 http://localhost:8080/swagger-ui/index.html

🧠 Tecnologias Utilizadas
| Categoria      | Tecnologia                      |
| :------------- | :------------------------------ |
| Linguagem      | **Java 21.0.5**                 |
| Framework      | **Spring Boot**                 |
| Segurança      | **Spring Security + JWT**       |
| Banco de Dados | **SQL Server**                  |
| ORM            | **Spring Data JPA (Hibernate)** |
| Migrações      | **Flyway**                      |
| Build          | **Maven**                       |
| Documentação   | **Swagger / OpenAPI**           |

🧭 Arquitetura do Projeto

🧩 Camadas Principais

Controller: expõe endpoints REST

Service: lógica de negócio

Repository: persistência via JPA

Entity/DTO/Mapper: estrutura e conversão de dados

Security: autenticação JWT e controle de acesso
