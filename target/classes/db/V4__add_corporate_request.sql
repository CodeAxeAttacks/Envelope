-- SQLBook: Code
CREATE TABLE IF NOT EXISTS corporate_requests (
    id SERIAL PRIMARY KEY,
    company_name VARCHAR(64) NOT NULL, 
    employee_count INTEGER NOT NULL CHECK (employee_count > 0),
    created_at TIMESTAMP NOT NULL,
    description VARCHAR(512) NOT NULL,
    vehicle_category VARCHAR(64) NOT NULL,
    transmission_type VARCHAR(64) NOT NULL,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE
);