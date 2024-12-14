-- Создание таблицы phonebooks
CREATE TABLE IF NOT EXISTS phonebooks (
    id SERIAL PRIMARY KEY
);

-- Создание таблицы users с внешним ключом на phonebooks
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phonebook_id INT NOT NULL,
    CONSTRAINT fk_phonebook_user FOREIGN KEY (phonebook_id) REFERENCES phonebooks(id) ON DELETE CASCADE
);

-- Создание таблицы contacts с внешним ключом на phonebooks
CREATE TABLE contacts (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    number VARCHAR(20) NOT NULL CHECK (number ~ '^\s*\+?\s*[\d\s]{3,15}\s*$'),
    phonebook_id INT NOT NULL,
    CONSTRAINT fk_phonebook_contact FOREIGN KEY (phonebook_id) REFERENCES phonebooks(id) ON DELETE CASCADE
);

-- Создание функции для автоматического добавления записи в phonebooks
CREATE OR REPLACE FUNCTION auto_create_phonebook()
RETURNS TRIGGER AS $$
DECLARE
    new_phonebook_id INT;
BEGIN
    -- Создаем новую запись в phonebooks
    INSERT INTO phonebooks DEFAULT VALUES RETURNING id INTO new_phonebook_id;

    -- Присваиваем новый phonebook_id в users
    NEW.phonebook_id = new_phonebook_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Создание триггера для таблицы users
CREATE TRIGGER trigger_auto_create_phonebook
BEFORE INSERT ON users
FOR EACH ROW
EXECUTE FUNCTION auto_create_phonebook();