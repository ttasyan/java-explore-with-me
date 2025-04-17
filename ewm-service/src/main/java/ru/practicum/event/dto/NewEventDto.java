package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.CategoryDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    private CategoryDto category;
    @NotNull
    private String title;
    @NotNull
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    private boolean paid;
    @NotNull
    private String description;
    @NotNull
    private LocationDto location;
    private int participationLimit;
    private boolean requestModeration;

}
