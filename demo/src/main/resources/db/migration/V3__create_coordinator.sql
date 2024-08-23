CREATE TABLE coordinator
(
    id     TEXT PRIMARY KEY UNIQUE NOT NULL,
    "user" TEXT                    NOT NULL,
    course VARCHAR(255)            NOT NULL,
    CONSTRAINT fk_course FOREIGN KEY (course) REFERENCES course (id),
    CONSTRAINT fk_user FOREIGN KEY ("user") REFERENCES users (id)
);