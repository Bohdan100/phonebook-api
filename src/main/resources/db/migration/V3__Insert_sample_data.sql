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
    ('Mike Johnson', '+71234567892', 1),
    ('Anna Brown', '+81234567893', 1),
    ('Chris Black', '+91234567894', 1),
    ('James Wilson', '+7123450101', 1),
    ('Charlotte Taylor', '+712340102', 1),
    ('Daniel Anderson', '+712340103', 1),
    ('Harper Thomas', '+712340104', 1),
    ('Benjamin Moore', '+712340105', 1);

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('Sarah White', '+71234567892', 2),
    ('George Martin', '+81234567893', 2),
    ('Lucas Green', '+7234567893', 2),
    ('Nina Roberts', '+834567894', 2),
    ('Liam Davis', '+934567895', 2),
    ('Evelyn Jackson', '+934550201', 2),
    ('Sebastian White', '+92340202', 2),
    ('Emily Harris', '+91234203', 2),
    ('Alexander Martin', '+9 12340204', 2),
    ('Scarlett Thompson', '+9 12340205', 2);

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('Olivia Clark', '+71234567894', 3),
    ('Mason Harris', '+81234567895', 3),
    ('Isabella Lewis', '+71234567894', 3),
    ('Ethan Walker', '+81234567896', 3),
    ('Ava King', '+91234567897', 3),
    ('Jack Garcia', '+15550301', 3),
    ('Luna Martinez', '+823550302', 3),
    ('Aiden Robinson', '+8435550303', 3),
    ('Chloe Clark', '+8315550304', 3),
    ('Owen Rodriguez', '+84515550305', 3);

INSERT INTO contacts (name, number, phonebook_id) VALUES
    ('Sophia Young', '+71234567896', 4),
    ('Lily Scott', '+81234567897', 4),
    ('Jacob Evans', '+71234567895', 4),
    ('Amelia Turner', '+81234567898', 4),
    ('Matthew Nelson', '+91234567899', 4),
    ('Mila Lewis', '+9842550401', 4),
    ('Wyatt Lee', '+98745550402', 4),
    ('Victoria Walker', '+98735550403', 4),
    ('Gabriel Hall', '+98214530404', 4),
    ('Hazel Allen', '+7592550405', 4);
