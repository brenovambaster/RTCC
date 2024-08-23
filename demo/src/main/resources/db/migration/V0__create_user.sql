drop table if exists users;
create table users
(
    id                 varchar(36)                                   not null primary key,
    name               varchar(50)                                   not null,
    password           varchar(255)                                  not null,
    email              varchar(100)                                  not null unique,
    role               varchar(50) default 'USER'::character varying not null,
    email_verified     boolean     default false                     not null,
    verification_token varchar(512)
);

alter table users
    owner to postgres;

INSERT INTO public.users (id, name, password, email, role)
VALUES ('c847d22f-8c7d-4e62-8321-e606d6caa0a4', 'coordinator',
        '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy', 'pedro.lima@ifnmg.com.br', 'COORDINATOR');
INSERT INTO public.users (id, name, password, email, role)
VALUES ('6c4f8d6e-1d2e-4b6c-b8b5-58b13ed6a6d1', 'admin', '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy',
        'joao.silva@ifnmg.com.br', 'ADMIN');
INSERT INTO public.users (id, name, password, email, role)
VALUES ('b287c44b-5aaf-45e3-8e14-0cfcff5b686b', 'user', '$2a$10$GiseHkdvwOFr7A9KRWbeiOmg/PYPhWVjdm42puLfOzR/gIAQrsAGy',
        'mariana.costa@ifnmg.com.br', 'USER');
