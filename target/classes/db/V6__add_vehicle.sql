-- SQLBook: Code
CREATE TABLE IF NOT EXISTS vehicles (
    id SERIAL PRIMARY KEY,
    model VARCHAR(64) NOT NULL, 
    year INTEGER NOT NULL CHECK (year > 1885), 
    transmission_type VARCHAR(64) NOT NULL,
    instructor_id INTEGER REFERENCES instructors(id)
);