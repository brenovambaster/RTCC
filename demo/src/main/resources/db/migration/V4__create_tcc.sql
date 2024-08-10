CREATE TABLE tcc
(
    id           TEXT PRIMARY KEY NOT NULL,
    title        VARCHAR(255)     NOT NULL,
    author       VARCHAR(255)     NOT NULL,
    course       VARCHAR(255)     NOT NULL,
    defense_date DATE             NOT NULL,
    advisor      TEXT             NOT NULL,
    summary      TEXT             NOT NULL,
    abstract     TEXT             NOT NULL,
    keywords     TEXT             NOT NULL,
    language     VARCHAR(50)      NOT NULL,
    path_file    VARCHAR(255)     NOT NULL,
    CONSTRAINT fk_course FOREIGN KEY (course) REFERENCES course (id),
    CONSTRAINT fk_advisor FOREIGN KEY (advisor) REFERENCES professor (id)
);

CREATE TABLE tcc_committee_members
(
    tcc_id       TEXT NOT NULL,
    professor_id TEXT NOT NULL,
    CONSTRAINT pk_tcc_committee PRIMARY KEY (tcc_id, professor_id),
    CONSTRAINT fk_tcc FOREIGN KEY (tcc_id) REFERENCES tcc (id) ON DELETE CASCADE,
    CONSTRAINT fk_professor FOREIGN KEY (professor_id) REFERENCES professor (id) ON DELETE CASCADE
);

-- ID está como TEXT pois O banco de dados não precisa saber que é um UUID.
-- Além disso, se migrar para outro banco de dados, o UUID pode ser gerado de outra forma ou não seja suportado.