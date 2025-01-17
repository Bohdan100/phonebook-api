package corp.phonebook.data.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @Size(min = 1, max = 255, message = "Name must be between 2 and 50 characters")
    private String name;

    @Size(min = 1, max = 255, message = "Name must be between 2 and 50 characters")
    private String email;
}
