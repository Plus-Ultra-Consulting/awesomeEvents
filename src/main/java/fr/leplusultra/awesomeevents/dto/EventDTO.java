package fr.leplusultra.awesomeevents.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventDTO {
    private int id;

    @NotEmpty
    @Setter(AccessLevel.NONE)
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @NotEmpty
    @Setter(AccessLevel.NONE)
    private String place;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    private LocalDateTime startAt;

    public void setName(String name) {
        this.name = name.trim();
    }

    public void setPlace(String place) {
        this.place = place.trim();
    }
}
