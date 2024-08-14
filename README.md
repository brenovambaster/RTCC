<h1 align="center">Repositório de TCCs do IFNMG-MOC (RTCC-IF)</h1>

<p align="center">
    <img src="https://img.shields.io/badge/License-MIT-blue.svg" alt="License MIT">
    <a href="https://github.com/brenovambaster/RTCC/issues">
        <img alt="GitHub Issues" src="https://img.shields.io/github/issues/brenovambaster/rtcc">
    </a>
    <a href="https://github.com/brenovambaster/RTCC/pulls"> 
        <img alt="GitHub Issues or Pull Requests" src="https://img.shields.io/github/issues-pr/brenovambaster/rtcc">
    </a>
</p>

<br>

## Descrição

Este projeto consiste em uma API destinada ao gerenciamento de Trabalhos de Conclusão de Curso (TCCs) do Instituto
Federal do Norte de Minas Gerais - Campus Montes Claros (IFNMG-MOC). A API possibilita o cadastro de TCCs, professores e
cursos, além de fornecer endpoints para a consulta de TCCs por curso, aluno e professor.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.2**
- **Maven**
- **PostgreSQL 16.3**
- **Flyway** (para migrações de banco de dados)
- **Swagger/OpenAPI** (para documentação da API)

## Requisitos

- **Java 17** ou superior
- **Maven**
- **PostgreSQL**

## Configuração do Banco de Dados

Para configurar o banco de dados, siga os passos abaixo:

1. **Instale e configure o PostgreSQL.**
2. **Crie um banco de dados chamado `rtcc`.**
3. **Configure as credenciais do banco de dados no arquivo `application.properties`, localizado no
   diretório `src/main/resources`:**

    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/rtcc
    spring.datasource.username=seu_usuario
    spring.datasource.password=sua_senha
    spring.jpa.hibernate.ddl-auto=update
    spring.flyway.enabled=true
    spring.flyway.locations=classpath:db/migration
    ```

   Substitua `seu_usuario` e `sua_senha` pelas credenciais do seu banco de dados PostgreSQL.

## Instalação

Para instalar e configurar o projeto em seu ambiente local, siga as etapas abaixo:

1. **Clone o repositório:**

    ```sh
    git clone https://github.com/seu_usuario/rtcc.git
    ```

2. **Navegue até o diretório do projeto:**

    ```sh
    cd rtcc
    ```

3. **Compile o projeto utilizando Maven:**

    ```sh
    mvn clean install
    ```

## Execução

Para rodar a aplicação, siga os passos:

1. **Inicie a aplicação:**

    ```sh
    mvn spring-boot:run
    ```

2. **A API estará disponível em** `http://localhost:8080`.

## Documentação da API

A documentação da API, gerada automaticamente pelo Swagger/OpenAPI, pode ser acessada
em `http://localhost:8080/swagger-ui.html`. Ela fornece uma interface interativa para testar os endpoints e visualizar
os detalhes da API.

## Contribuição

Se você deseja contribuir com o desenvolvimento deste projeto, siga os passos abaixo:

1. **Faça um fork do projeto.**
2. **Crie uma branch para sua nova feature:**

    ```sh
    git checkout -b feature/nova-feature
    ```

3. **Commit suas alterações:**

    ```sh
    git commit -am 'Adiciona nova feature'
    ```

4. **Envie as alterações para sua branch remota:**

    ```sh
    git push origin feature/nova-feature
    ```

5. **Abra um Pull Request no repositório original.**

## Licença

Este projeto está licenciado sob os termos da licença MIT. Para mais detalhes, consulte o arquivo `LICENSE`.
