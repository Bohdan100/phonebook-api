package corp.phonebook.data.entity;

import jakarta.persistence.*;
import lombok.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "phonebooks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "contacts")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PhoneBook implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(orphanRemoval = true, mappedBy = "phoneBook")
    private User owner;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "phoneBook")
    private List<Contact> contacts;
}
