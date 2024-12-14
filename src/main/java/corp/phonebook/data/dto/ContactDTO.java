package corp.phonebook.data.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ContactDTO {
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "Number cannot be blank")
    @Pattern(regexp = "^\\s*\\+?\\s*[\\d\\s]{3,15}\\s*$", message = "Invalid phone number format")
    private String number;

    @NotNull(message = "Phonebook ID cannot be null")
    private Long phonebookId;
}