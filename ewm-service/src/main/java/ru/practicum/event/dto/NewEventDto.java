package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    private Long category;
    @NotNull
    private String title;
    @NotNull
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    private Boolean paid = false;
    @NotNull
    private String description;
    @NotNull
    private LocationDto location;
    @PositiveOrZero
    private Integer participationLimit = 0;
    private Boolean requestModeration = true;

}
