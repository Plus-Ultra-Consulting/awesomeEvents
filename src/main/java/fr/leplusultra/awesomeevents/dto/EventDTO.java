package fr.leplusultra.awesomeevents.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventDTO {
    private int id;

    @NotEmpty
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createdAt;

    @NotEmpty
    private String place;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @NotNull
    private Date startAt;
}
