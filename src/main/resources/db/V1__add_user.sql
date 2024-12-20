-- SQLBook: Code
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(64) UNIQUE NOT NULL,
    password VARCHAR(64) NOT NULL,
    firstname VARCHAR(64) NOT NULL,
    lastname VARCHAR(64) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    status VARCHAR(64) NOT NULL,
    role VARCHAR(64) NOT NULL
);

