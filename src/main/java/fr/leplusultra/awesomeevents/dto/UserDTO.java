package fr.leplusultra.awesomeevents.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.leplusultra.awesomeevents.util.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDTO {
    private int id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdAt;

    private UserRole role;
}
