package corp.phonebook.data.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "phoneBook")
public class Contact implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^\\s*\\+?\\s*[\\d\\s]{3,15}\\s*$")
    private String number;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phonebook_id", nullable = false)
    PhoneBook phoneBook;

    public Contact(String name, String number, PhoneBook phoneBook) {
        this.name = name;
        this.number = number;
        this.phoneBook = phoneBook;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return Objects.equals(id, contact.id) &&
                Objects.equals(name, contact.name) &&
                Objects.equals(number, contact.number) &&
                Objects.equals(phoneBook, contact.phoneBook);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, number, phoneBook);
    }
}