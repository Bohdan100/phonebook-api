-- Добавление данных в таблицу users
INSERT INTO users (name, phonebook_id) VALUES
    ('Alice', 1),
    ('Bob', 2);

-- Добавление данных в таблицу contacts
INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('John Doe', '+71234567890', 1),
    ('Jane Smith', '+81234567891', 1),
    ('Mike Johnson', '+7 1234567892', 2),
    ('Anna Brown', '+8 1234567893', 2);
