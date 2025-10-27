# 🧱 Backend — Sistema de Gestão de Usuários

> API em **Java 21.0.5 + Spring Boot**, com autenticação **JWT**, migrações automáticas via **Flyway** e banco **SQL Server**.  
> Projeto desenvolvido para o **Desafio Técnico – Sistema de Gestão de Usuários (Britech)**.

---

## ⚙️ Como instalar

**Pré-requisitos**
- ☕ Java 21.0.5  
- 🧰 Maven  
- 🗄️ SQL Server  
- 🌀 Git  

**Configuração**
1. Crie o banco:
   ```sql
   CREATE DATABASE gestao_usuarios;
Crie src/main/resources/application-local.yml:

yaml
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
Rode o projeto:

bash
./mvnw clean install
./mvnw spring-boot:run
🔗 API: http://localhost:8080

🧩 Tecnologias
Java 21.0.5

Spring Boot (Web, Security, JPA/Hibernate)

SQL Server

Flyway (migrações)

JWT (autenticação)

Maven

🚀 Principais funcionalidades
🔐 Login e autenticação via JWT

👤 Cadastro e gerenciamento de usuários

📋 CRUD completo (criar, listar, editar, excluir)

🧱 Migrações automáticas com Flyway

⚙️ Rotas protegidas e validações

📡 API RESTful modular e documentada

🧭 Arquitetura
bash
src/main/java/com/gestao/usuarios/
 ├── security/   # JWT e filtros
 ├── user/       # Entidade, DTOs, Mapper e Repository
 ├── web/        # Controllers REST
 └── config/     # Configurações e Swagger
Camadas: Controller → Service → Repository → Entity
Padrões: DTOs · Mapper · JWT Filter · Flyway Migrations

