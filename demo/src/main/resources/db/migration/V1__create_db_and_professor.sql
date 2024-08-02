CREATE TABLE professor
(
    id             TEXT PRIMARY KEY UNIQUE NOT NULL,
    name           VARCHAR(255)            NOT NULL,
    email          VARCHAR(255)            NOT NULL,
    research_area  VARCHAR(255)            NOT NULL,
    title          VARCHAR(255)            NOT NULL,
    location_of_work VARCHAR(255)            NOT NULL
);
