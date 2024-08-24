CREATE TABLE IF NOT EXISTS coordinator
(
    id        TEXT PRIMARY KEY UNIQUE NOT NULL,
    user_id   TEXT                    NOT NULL,
    course_id VARCHAR(255)            NOT NULL,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES course (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);