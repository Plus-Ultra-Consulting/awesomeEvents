package fr.leplusultra.awesomeevents.model.user;

import fr.leplusultra.awesomeevents.model.event.Event;
import fr.leplusultra.awesomeevents.util.UserRole;
import jakarta.persistence.*;
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
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    private String password;

    @Enumerated(EnumType.ORDINAL)
    private UserRole role;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Event> events;
}
