CREATE INDEX idx_users_session_id ON users(session_id);
CREATE INDEX idx_users_name ON users(name);
CREATE INDEX idx_contacts_phonebook_id ON contacts(phonebook_id);
CREATE INDEX idx_contacts_name ON contacts(name);
CREATE INDEX idx_contacts_number ON contacts(number);

CREATE INDEX idx_user_audit_operation ON user_audit(operation);
CREATE INDEX idx_user_audit_user_id ON user_audit(user_id);