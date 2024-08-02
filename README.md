# RTCC - Repositório TCC IFNMG

## Descrição

Este projeto é uma API para gerenciar cursos e professores, desenvolvida utilizando Java, Spring Boot, e PostgreSQL. A
API permite criar, listar, buscar e deletar cursos e professores.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.3.2
- Maven
- PostgreSQL
- Flyway
- Swagger/OpenAPI

## Requisitos

- Java 17 ou superior
- Maven
- PostgreSQL

## Configuração do Banco de Dados

1. Instale e configure o PostgreSQL.
2. Crie um banco de dados chamado `rtcc`.
3. Configure as credenciais do banco de dados no arquivo `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/rtcc
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

## Instalação

1. Clone o repositório:
    ```sh
    git clone https://github.com/seu_usuario/rtcc.git
    ```
2. Navegue até o diretório do projeto:
    ```sh
    cd rtcc
    ```
3. Compile o projeto utilizando Maven:
    ```sh
    mvn clean install
    ```

## Execução

1. Execute a aplicação:
    ```sh
    mvn spring-boot:run
    ```
2. A API estará disponível em `http://localhost:8080`.

## Endpoints

### Cursos

- **POST /course**: Salvar um novo curso.
- **GET /course**: Listar todos os cursos.
- **GET /course/{id}**: Buscar um curso pelo ID.
- **DELETE /course/{id}**: Deletar um curso pelo ID.

### Professores

- **POST /professor**: Salvar um novo professor.
- **GET /professor**: Listar todos os professores.
- **GET /professor/{id}**: Buscar um professor pelo ID.
- **DELETE /professor/{id}**: Deletar um professor pelo ID.

## Documentação da API

A documentação da API está disponível em `http://localhost:8080/swagger-ui.html`.

## Contribuição

1. Faça um fork do projeto.
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`).
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`).
4. Faça o push para a branch (`git push origin feature/nova-feature`).
5. Crie um novo Pull Request.

## Licença

Este projeto está licenciado sob os termos da licença MIT. Veja o arquivo `LICENSE` para mais detalhes.