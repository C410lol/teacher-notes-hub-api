# Teacher Notes Hub — API

API REST do **Teacher Notes Hub**, uma aplicação voltada à organização de informações e rotinas escolares. O backend concentra as regras de negócio e a persistência dos dados utilizados pela interface web, oferecendo recursos para gerenciar usuários, instituições, professores, alunos, cadernetas, aulas, frequência, notas e atividades.

O projeto foi desenvolvido com Java e Spring Boot, utilizando uma arquitetura em camadas para separar controllers, serviços, repositórios, entidades e objetos de transferência de dados (DTOs).

## ✨ Funcionalidades

Entre os recursos implementados na API estão:

- Gerenciamento de usuários, professores, administradores e instituições.
- Criação, consulta, edição e finalização de cadernetas escolares.
- Cadastro e organização de alunos.
- Gerenciamento de aulas e conteúdos relacionados.
- Registro e consulta de frequência dos estudantes.
- Gerenciamento de notas e atividades.
- Consulta de desempenho dos alunos e identificação de tarefas pendentes.
- Recursos relacionados a códigos da BNCC.
- Utilitários para processamento e geração de arquivos de planilha.
- Autenticação e autorização com Spring Security e JWT.

A disponibilidade de cada operação depende das regras de acesso implementadas para os diferentes perfis de usuário.

## 🧰 Tecnologias

- **Java 20**
- **Spring Boot 3**
- Spring Web
- Spring Data JPA
- Spring Security
- Bean Validation
- **JWT (JSON Web Tokens)**
- **PostgreSQL**
- Hibernate
- Apache POI
- Maven
- Lombok

## 🏗️ Organização da aplicação

O código está organizado por responsabilidade:

- `controllers`: endpoints REST e tratamento das requisições HTTP.
- `services`: regras de negócio e orquestração das operações.
- `repositories`: acesso e persistência dos dados com Spring Data JPA.
- `models/entities`: entidades persistidas no banco de dados.
- `models/dtos`: objetos utilizados na comunicação entre a API e os clientes.
- `configs`: configurações gerais e de segurança.
- `utils`: utilitários para operações auxiliares, incluindo tratamento de cadernetas e planilhas.

## ▶️ Como executar localmente

### Pré-requisitos

- JDK 20.
- PostgreSQL.
- Git.

### 1. Clone o repositório

```bash
git clone https://github.com/C410lol/teacher-notes-hub-api.git
cd teacher-notes-hub-api
```

### 2. Configure o banco de dados

Crie um banco de dados PostgreSQL e configure a conexão da aplicação em:

```
src/main/resources/application.properties
```

Confira também as configurações de segurança e os parâmetros necessários à geração e validação de tokens JWT. Utilize valores próprios para seu ambiente local e não publique credenciais ou chaves secretas no repositório.

### 3. Execute a aplicação

No Linux ou macOS:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
mvnw.cmd spring-boot:run
```

### 4. Execute os testes

```bash
./mvnw test
```

## 🔗 Projeto relacionado

- [Teacher Notes Hub — Frontend Angular](https://github.com/C410lol/teacher-notes-hub-front)

---

Este repositório contém o backend do Teacher Notes Hub. Para utilizar a solução completa, consulte também o projeto frontend.
