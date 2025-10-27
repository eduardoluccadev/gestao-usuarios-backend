# 🧱 Backend — Sistema de Gestão de Usuários


> API desenvolvida em **Java 21.0.5 + Spring Boot**, com autenticação **JWT**, migrações automáticas via **Flyway** e banco de dados **SQL Server**. 
> Projeto criado para o **Desafio Técnico – Sistema de Gestão de Usuários (Britech)**.

---

## 🚀 Tecnologias Utilizadas

| Categoria | Tecnologia | Versão | Descrição |
| :---: | :---: | :---: | :--- |
| **Linguagem** | ☕ Java | 21.0.5 | Ambiente de execução. |
| **Framework** | ⚙️ Spring Boot | 3.3.1 | Desenvolvimento da API REST. |
| **Persistência** | 💾 Spring Data JPA / Hibernate | - | Mapeamento Objeto-Relacional. |
| **Banco de Dados** | 🗄️ SQL Server | - | Banco de dados relacional. |
| **Migrações** | 🧱 Flyway | - | Controle de versão do banco de dados. |
| **Segurança** | 🔐 Spring Security / JWT | - | Autenticação e autorização. |
| **Documentação** | 📘 Swagger / OpenAPI | - | Geração de documentação da API. |
| **Build** | 🧰 Maven | - | Gerenciamento de dependências e build. |

---

## 💡 Principais Funcionalidades

* 🔐 **Autenticação e Autorização:** Implementação completa com JWT (JSON Web Tokens).
* 👤 **Gestão de Usuários:** CRUD completo (criar, listar, editar, excluir).
* 📋 **Fluxo de Acesso:** Cadastro, login, recuperação de senha e controle de sessão (`/logout`).
* 🧱 **Controle de Banco de Dados:** Migrações automáticas via Flyway, garantindo a evolução do schema.
* ⚙️ **Robustez:** Validação de dados e regras de negócio para garantir a integridade.

---

## 🧠 Estrutura da Arquitetura

O projeto segue a arquitetura em camadas (Controller, Service, Repository) com foco em separação de responsabilidades:

* **🧩 Segurança:** Autenticação e autorização via Spring Security, implementando `JwtAuthFilter` e `JwtService`.
* **🌐 Controllers:** Camada de entrada, responsável por receber requisições e expor os *endpoints* REST.
* **🧠 Services:** Contém a lógica de negócio e orquestração de operações.
* **🗃️ Repositories:** Responsável pela persistência de dados com Spring Data JPA.
* **🧱 Migrações:** Scripts SQL versionados localizados em `src/main/resources/db/migration`.

---

## ⚙️ Como Instalar e Rodar o Projeto

### Pré-requisitos

☕ Java 21.0.5
🧰 Maven
🗄️ SQL Server
🌀 Git

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/eduardoluccadev/gestao-usuarios-backend.git
cd gestao-usuarios-backend

2️⃣ Criar o Banco de Dados
Execute o comando SQL a seguir no seu SQL Server:
CREATE DATABASE gestao_usuarios;

3️⃣ Configurar o application.yml
Crie e preencha o arquivo de configuração
src/main/resources/application.yml com suas credenciais locais:
YAML
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
    secret: SUA_CHAVE_SECRETA # Gere uma chave segura e longa!
    expirationMinutes: 60

4️⃣
Instalar dependências e Iniciar
Bash
# Instala dependências e faz o build
./mvnw clean install

# Roda a aplicação
./mvnw spring-boot:run

5️⃣ Acessar a API e Documentação
Recurso --> EndpointAPI
Principal --> http://localhost:8080
Documentação Swagger --> http://localhost:8080/swagger-ui/index.html
