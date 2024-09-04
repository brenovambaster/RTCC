CREATE TABLE IF NOT EXISTS ACADEMIC_LIKES_TCC
(
    TCC_ID      TEXT NOT NULL,
    ACADEMIC_ID TEXT NOT NULL,
    CONSTRAINT PK_ACADEMIC_LIKES_TCC PRIMARY KEY (TCC_ID, ACADEMIC_ID),
    CONSTRAINT TCC_ID FOREIGN KEY (TCC_ID) REFERENCES tcc (id) ON DELETE CASCADE,
    CONSTRAINT ACACEMIC_ID FOREIGN KEY (ACADEMIC_ID) REFERENCES academic (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ACADEMIC_FAVORITES
(
    TCC_ID      TEXT NOT NULL,
    ACADEMIC_ID TEXT NOT NULL,
    CONSTRAINT PK_ACADEMIC_FAVORITES PRIMARY KEY (TCC_ID, ACADEMIC_ID),
    CONSTRAINT TCC_ID FOREIGN KEY (TCC_ID) REFERENCES tcc (id) ON DELETE CASCADE,
    CONSTRAINT ACADEMIC_ID FOREIGN KEY (ACADEMIC_ID) REFERENCES academic (user_id) ON DELETE CASCADE
);