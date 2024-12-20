-- SQLBook: Code
CREATE TABLE IF NOT EXISTS courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    price DECIMAL NOT NULL CHECK (price >= 0),
    duration DECIMAL NOT NULL CHECK (duration > 0),
    description VARCHAR(512) NOT NULL,
    category VARCHAR(64) NOT NULL,
    transmission VARCHAR(64) NOT NULL,
    study_format VARCHAR(64) NOT NULL,
    driving_school_id INTEGER REFERENCES driving_schools(id) ON DELETE CASCADE
);
