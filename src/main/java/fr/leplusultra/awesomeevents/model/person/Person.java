package fr.leplusultra.awesomeevents.model.person;

import fr.leplusultra.awesomeevents.model.event.Event;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "event")
@EqualsAndHashCode
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column()
    @Setter(AccessLevel.NONE)
    private String email;

    @Column(name = "creation_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @Column(name = "qrcode_use_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime securityCodeActivatedAt;

    @JoinColumn(referencedColumnName = "id", name = "event_id")
    @ManyToOne
    private Event event;

    @Column(name = "security_code", unique = true)
    private String securityCode;

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase() : null;
    }
}
