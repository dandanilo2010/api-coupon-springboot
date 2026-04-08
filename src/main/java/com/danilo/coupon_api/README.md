# Coupon API

API REST para gerenciamento de cupons desenvolvida com Java e Spring Boot.

## Tecnologias utilizadas

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

## Regras de negócio

### Create
Um cupom pode ser cadastrado a qualquer momento e possui como obrigatórios os campos:
- code
- description
- discountValue
- expirationDate

### Regras do code
- O código do cupom deve possuir 6 caracteres alfanuméricos
- Caracteres especiais são aceitos na entrada, mas são removidos antes de salvar e retornar na resposta

Exemplo:
`ABC-12@3 -> ABC123`

### Regras do desconto
- O valor mínimo do desconto é 0.5

### Regras da data de expiração
- O cupom não pode ser criado com data de expiração no passado

### Delete
- O cupom utiliza soft delete
- Não é removido fisicamente do banco
- Não é permitido deletar um cupom já deletado

## Arquitetura do projeto

O projeto foi organizado nas seguintes camadas:

- controller
- service
- domain
- repository
- dto
- exception

As principais regras de negócio foram implementadas dentro da entidade Coupon.
- `Coupon.create(...)`
- `coupon.delete()`

## Endpoints

### Criar cupom
`POST /coupon`

### Buscar cupom por id
`GET /coupon/{id}`

### Deletar cupom
`DELETE /coupon/{id}`

## Exemplo de requisição

json
{
  "code": "ABC-12@3",
  "description": "Cupom de teste",
  "discountValue": 10.5,
  "expirationDate": "2026-04-10T10:00:00",
  "published": true
}

Exemplo de resposta
{
  "id": "uuid-gerado",
  "code": "ABC123",
  "description": "Cupom de teste",
  "discountValue": 10.5,
  "expirationDate": "2026-04-10T10:00:00",
  "status": "ACTIVE",
  "published": true,
  "redeemed": false
}
Como executar o projeto localmente
Rodar a aplicação
./mvnw spring-boot:run
Rodar os testes
./mvnw test
Gerar o jar
./mvnw clean package
Swagger

A documentação da API fica disponível em:
http://localhost:8080/swagger-ui.html

H2 Console

O console do banco fica disponível em:
http://localhost:8080/h2-console

Como executar com Docker
Build da imagem
docker build -t coupon-api .
Rodar container
docker run -p 8081:8080 coupon-api
Como executar com Docker Compose
docker compose up --build
- Observações
O projeto utiliza H2 em memória
Os testes cobrem as principais regras de negócio da aplicação
O soft delete é tratado para evitar perda de dados