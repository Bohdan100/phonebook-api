INSERT INTO users (name, email, password, role) VALUES
    --password: hashed_password_1
    ('Alice', 'alice@example.com', '$2b$12$wwjDuaJgfKD8ssTXlGK3nOK.3qqzT2q79K3PZGPFjSRpb4VBSvx5m', 'USER'),
    --password: hashed_password_2
    ('Bob', 'bob@example.com', '$2b$12$1XGcDssNHOACVlSTlgaYgu5oI83cVX0mucx0yn1pg/3tKnsCThKES', 'USER'),
    --password: hashed_password_3
    ('Charlie', 'charlie@example.com', '$2a$10$167EvdSyQu9IZb8ij8Hz3uhrK64njBK0do9yZKDfRjWGLAFYGnXPC', 'USER'),
    --password: hashed_password_4
    ('David', 'david@example.com', '$2a$10$Jt7YS0C.XaY3J6YpA8/Tv.MCNx8AUEhSgkis0hUNiQrRM3.VvfI3a', 'ADMIN');

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('John Doe', '+71234567890', 1),
    ('Jane Smith', '+81234567891', 1),
    ('Mike Johnson', '+7 1234567892', 1),
    ('Anna Brown', '+8 1234567893', 1),
    ('Chris Black', '+9 1234567894', 1);

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('Sarah White', '+71234567892', 2),
    ('George Martin', '+81234567893', 2),
    ('Lucas Green', '+7 1234567893', 2),
    ('Nina Roberts', '+8 1234567894', 2),
    ('Liam Davis', '+9 1234567895', 2);

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('Olivia Clark', '+71234567894', 3),
    ('Mason Harris', '+81234567895', 3),
    ('Isabella Lewis', '+7 1234567894', 3),
    ('Ethan Walker', '+8 1234567896', 3),
    ('Ava King', '+9 1234567897', 3);

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('Sophia Young', '+71234567896', 4),
    ('Lily Scott', '+81234567897', 4),
    ('Jacob Evans', '+7 1234567895', 4),
    ('Amelia Turner', '+8 1234567898', 4),
    ('Matthew Nelson', '+9 1234567899', 4);
