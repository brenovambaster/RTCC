CREATE TABLE files
(    id             TEXT PRIMARY KEY UNIQUE NOT NULL,
     name           VARCHAR(255)            NOT NULL,
     type           VARCHAR(255)            NOT NULL,
     path           VARCHAR(255)            NOT NULL
);
