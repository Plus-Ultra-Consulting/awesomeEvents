package fr.leplusultra.awesomeevents.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PersonDTO {
    private int id;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Email
    @NotEmpty
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date qrUsedAt;

    private int eventId;
}
