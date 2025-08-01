package fr.leplusultra.awesomeevents.model.user;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.model.otp.OTP;
import fr.leplusultra.awesomeevents.model.token.Token;
import fr.leplusultra.awesomeevents.util.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"events", "token", "otp"})
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    @Setter(AccessLevel.NONE)
    private String firstName;

    @Column(name = "last_name")
    @Setter(AccessLevel.NONE)
    private String lastName;

    @Column(unique = true, nullable = false)
    @Setter(AccessLevel.NONE)
    private String email;

    @Column(name = "creation_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Token token;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private OTP otp;

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase().trim() : null;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }
}
