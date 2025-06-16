package fr.leplusultra.awesomeevents.model.event;

import fr.leplusultra.awesomeevents.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String name;
    @Column(name = "creation_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    @NotNull
    private Date createdAt;
    @NotEmpty
    private String place;
    @Column(name = "start_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm")
    @NotNull
    private Date startDateTime;

    @JoinColumn(referencedColumnName = "id", name = "user_id")
    @ManyToOne
    @NotNull
    private User user;
}
