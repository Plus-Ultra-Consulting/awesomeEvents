package fr.leplusultra.awesomeevents.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.leplusultra.awesomeevents.util.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {

    @NotEmpty
    @Size(min = 3, max = 20, message = "First name length must be between 3 and 20 characters")
    private String firstName;

    @NotEmpty
    @Size(min = 3, max = 20, message = "Last name length must be between 3 and 20 characters")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    @NotNull
    private Date createdAt;

    @NotEmpty
    @Size(min = 6, max = 16, message = "password length must be between 6 and 16 characters")
    private String password;

    @NotNull
    private UserRole role;

    private List<EventDTO> events;
}
