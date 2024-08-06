CREATE TABLE coordinator
(
    id        TEXT PRIMARY KEY UNIQUE NOT NULL,
    name      VARCHAR(255) NOT NULL,
    email     VARCHAR(255) NOT NULL,
    username  VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    course    VARCHAR(255) NOT NULL
);