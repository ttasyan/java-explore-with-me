package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.category.CategoryDto;
import ru.practicum.event.model.EventStateAction;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UpdateEventAdminRequest {

    private CategoryDto category;
    private String title;
    private String annotation;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;

    private Boolean paid;
    private Integer participationLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
}
