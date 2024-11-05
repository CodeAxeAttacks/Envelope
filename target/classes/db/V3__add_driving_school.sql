-- SQLBook: Code
CREATE TABLE IF NOT EXISTS driving_schools (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) UNIQUE NOT NULL,
    address VARCHAR(128) NOT NULL,
    phone_number VARCHAR(12) NOT NULL,
    email VARCHAR(64) NOT NULL UNIQUE,
    description VARCHAR(512) NOT NULL,
    rating DECIMAL NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS additional_services (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    description VARCHAR(512),
    price DECIMAL NOT NULL CHECK (price >= 0),
    driving_school_id INTEGER REFERENCES driving_schools(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS driving_school_reviews (
    id SERIAL PRIMARY KEY,
    rate DECIMAL NOT NULL CHECK (rate >= 0 AND rate <= 5),
    review VARCHAR(512) NOT NULL,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    driving_school_id INTEGER REFERENCES driving_schools(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS admin_driving_school (
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    driving_school_id INTEGER REFERENCES driving_schools(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, driving_school_id)
);

CREATE TABLE IF NOT EXISTS instructor_driving_school (
    instructor_id INTEGER REFERENCES instructors(id) ON DELETE CASCADE,
    driving_school_id INTEGER REFERENCES driving_schools(id) ON DELETE CASCADE,
    PRIMARY KEY (instructor_id, driving_school_id)
);
