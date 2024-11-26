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

CREATE OR REPLACE FUNCTION update_instructor_rating()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE instructors
    SET rating = (
        SELECT AVG(rate)
        FROM instructor_reviews
        WHERE instructor_id = NEW.instructor_id
    )
    WHERE id = NEW.instructor_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_instructor_rating_trigger 
AFTER INSERT OR UPDATE ON instructor_reviews
FOR EACH ROW
EXECUTE FUNCTION update_instructor_rating();
