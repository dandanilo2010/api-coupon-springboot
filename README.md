# Coupon API

API REST para gerenciamento de cupons desenvolvida com **Java e Spring Boot**.

---

# Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- Swagger / OpenAPI
- JUnit 5
- Mockito
- Docker
- Docker Compose

---

# Regras de negócio

## Create

Um cupom pode ser cadastrado a qualquer momento e possui como obrigatórios os campos:

- code
- description
- discountValue
- expirationDate

### Regras do code

O código do cupom deve possuir **6 caracteres alfanuméricos**.

Caracteres especiais são aceitos na entrada, mas são removidos antes de salvar e retornar na resposta.
Exemplo :
ABC-12@3 -> ABC123


### Regras do desconto

O valor mínimo do desconto é **0.5**.

### Regras da data de expiração

O cupom não pode ser criado com **data de expiração no passado**.

---

## Delete

O cupom utiliza **soft delete**, não sendo removido fisicamente do banco.

---

# Arquitetura do projeto

O projeto foi organizado nas seguintes camadas:

- controller → endpoints da API
- service → regras de negócio
- domain → entidade com validações
- repository → acesso ao banco
- dto → objetos de entrada e saída
- exception → tratamento de erros

---

# Exemplo de requisição

```json
{
  "code": "ABC-12@3",
  "description": "Cupom de teste",
  "discountValue": 10.5,
  "expirationDate": "2026-04-10T10:00:00",
  "published": true
}
```

# Como executar o projeto
Rodar aplicação
./mvnw spring-boot:run
Rodar testes
./mvnw test
Gerar jar
./mvnw clean package

# Swagger
http://localhost:8080/swagger-ui.html

#H2 Console
http://localhost:8080/h2-console

# Docker

Build da imagem:

docker build -t coupon-api .

Rodar container:

docker run -p 8081:8080 coupon-api

# Docker Compose
docker compose up --build
