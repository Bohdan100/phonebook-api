package corp.phonebook.data.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
