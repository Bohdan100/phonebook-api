Create a new user in PostgreSQL and set up the database before running the application "phonebook-api":
1. Create a new user with a password:  CREATE USER admin WITH PASSWORD 'secret1234';
2. Create a new database:   CREATE DATABASE phonebook;
3. Assign ownership of the database to the new user:   ALTER DATABASE phonebook OWNER TO admin;
4. Connect to the newly created database:  \c phonebook   
5. Grant schema usage and creation privileges to the user:   GRANT USAGE ON SCHEMA public TO admin;   GRANT CREATE ON SCHEMA public TO admin;
6. Grant all privileges on tables and sequences in the schema:   GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO admin;    GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO admin; 
7. Configure default privileges for new tables and sequences:  ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO admin; ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO admin;

To connect to the 'phonebook' database as the 'admin' user, use the following command in the terminal: psql -U admin -d phonebook
