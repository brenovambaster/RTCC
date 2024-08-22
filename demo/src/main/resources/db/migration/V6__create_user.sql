drop table if exists users;
CREATE TABLE users
(
    id       VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50)  NOT NULL,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE
);
INSERT INTO users (id, username, email, password)
VALUES ('6c4f8d6e-1d2e-4b6c-b8b5-58b13ed6a6d1', 'username', 'joao.silva@ifnmg.com.br',
        '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy'),
       ('b287c44b-5aaf-45e3-8e14-0cfcff5b686b', 'Mariana Costa', 'mariana.costa@ifnmg.com.br',
        '$2a$10$C7h7EFiFJ8JkG8z.E/5u6OXZj/cX0BThw/8cz7Tr3ocMy7GFuLp2K'),
       ('c847d22f-8c7d-4e62-8321-e606d6caa0a4', 'Pedro Lima', 'pedro.lima@ifnmg.com.br',
        '$2a$10$K9VfDRouOB.tV9g6m9RlyOJ2.rffN1mdf88gyS2bMa/Uo1d6fTceO')
;