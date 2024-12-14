package corp.phonebook.data.entity;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "phoneBook")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phonebook_id", nullable = false)
    PhoneBook phoneBook;

    public User(String name) {
        this.name = name;
    }
}
