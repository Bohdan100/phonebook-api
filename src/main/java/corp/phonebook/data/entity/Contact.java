package corp.phonebook.data.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;

@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "phoneBook")
@EqualsAndHashCode(exclude = "phoneBook")
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
}