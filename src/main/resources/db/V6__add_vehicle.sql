CREATE TABLE IF NOT EXISTS vehicle (
    id SERIAL PRIMARY KEY,
    model VARCHAR(64) NOT NULL, 
    year INTEGER NOT NULL CHECK (year > 1885), 
    transmission_type VARCHAR(64) NOT NULL
)