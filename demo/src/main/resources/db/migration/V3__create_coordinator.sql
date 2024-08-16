CREATE TABLE coordinator
(
    id       TEXT PRIMARY KEY UNIQUE NOT NULL,
    name     VARCHAR(255)            NOT NULL,
    email    VARCHAR(255) UNIQUE     NOT NULL,
    username VARCHAR(255) UNIQUE     NOT NULL,
    password VARCHAR(255)            NOT NULL,
    course   VARCHAR(255)            NOT NULL,
    CONSTRAINT fk_course FOREIGN KEY (course) REFERENCES course (id)
);