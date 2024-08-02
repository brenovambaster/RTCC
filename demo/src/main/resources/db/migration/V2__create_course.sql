CREATE TABLE course
(
    id             TEXT PRIMARY KEY UNIQUE NOT NULL,
    name           VARCHAR(255)            NOT NULL,
    campus         TEXT                    NOT NULL,
    code_of_course VARCHAR(255) UNIQUE     NOT NULL
);