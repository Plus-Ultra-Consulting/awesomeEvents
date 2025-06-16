package fr.leplusultra.awesomeevents.model.user;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.util.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    @NotEmpty
    @Size(min = 3, max = 20, message = "First name length must be between 3 and 20 characters")
    private String firstName;
    @Column(name = "last_name")
    @NotEmpty
    @Size(min = 3, max = 20, message = "Last name length must be between 3 and 20 characters")
    private String lastName;
    @Column(unique = true, nullable = false)
    @NotEmpty
    @Email
    private String email;
    @NotNull
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @NotEmpty
    @Size(min = 6, max = 100, message = "password length must be between 6 and 100 characters")
    private String password;

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events;
}
