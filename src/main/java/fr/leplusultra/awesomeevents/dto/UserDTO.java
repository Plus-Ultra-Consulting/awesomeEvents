package fr.leplusultra.awesomeevents.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.leplusultra.awesomeevents.util.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

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
    @Setter(AccessLevel.NONE)
    private String email;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private UserRole role;

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase() : null;
    }
}
