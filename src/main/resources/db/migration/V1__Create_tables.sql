CREATE TABLE IF NOT EXISTS phonebooks (
    id SERIAL PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    session_id VARCHAR(500),
    session_expiration TIMESTAMP,
    role VARCHAR(255) NOT NULL,
    phonebook_id INT NOT NULL,
    CONSTRAINT fk_phonebook_user FOREIGN KEY (phonebook_id) REFERENCES phonebooks(id) ON DELETE CASCADE
);

CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    number VARCHAR(20) NOT NULL CHECK (number ~ '^\s*\+?\s*[\d\s]{3,15}\s*$'),
    phonebook_id INT NOT NULL,
    CONSTRAINT fk_phonebook_contact FOREIGN KEY (phonebook_id) REFERENCES phonebooks(id) ON DELETE CASCADE
);


CREATE OR REPLACE FUNCTION auto_create_phonebook()
RETURNS TRIGGER AS $$
DECLARE
    new_phonebook_id INT;
BEGIN
    INSERT INTO phonebooks DEFAULT VALUES RETURNING id INTO new_phonebook_id;

    NEW.phonebook_id = new_phonebook_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create phonebook with user
CREATE TRIGGER trigger_auto_create_phonebook
BEFORE INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION auto_create_phonebook();