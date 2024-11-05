-- SQLBook: Code
CREATE TABLE IF NOT EXISTS instructors (
    id SERIAL PRIMARY KEY,
    experience INTEGER CHECK (experience >= 0),
    description VARCHAR(512),
    rating DECIMAL CHECK (rating >= 0),
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS instructor_services (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(512) NOT NULL,
    price DECIMAL NOT NULL CHECK (price >= 0),
    instructor_id INTEGER REFERENCES instructors(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS instructor_reviews (
    id SERIAL PRIMARY KEY,
    rate DECIMAL NOT NULL CHECK (rate >= 0 AND rate <= 5),
    review VARCHAR(512) NOT NULL,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    instructor_id INTEGER REFERENCES instructors(id) ON DELETE CASCADE
);