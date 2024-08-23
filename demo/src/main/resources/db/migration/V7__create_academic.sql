CREATE TABLE academic
(
    id        TEXT PRIMARY KEY NOT NULL,
    course_id TEXT             NOT NULL,
    user_id   TEXT             NOT NULL,
    FOREIGN KEY (course_id) REFERENCES course (id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
)