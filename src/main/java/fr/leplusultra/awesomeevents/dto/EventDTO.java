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
    @NotEmpty
    private String name;
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    private Date createdAt;
    private String place;
    @JsonFormat(pattern = "MM/dd/yyyy HH:mm")
    @NotNull
    private Date startAt;
}
