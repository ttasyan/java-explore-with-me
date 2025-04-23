package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.CategoryDto;
import ru.practicum.user.UserShortDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    private Long id;

    private CategoryDto category;
    private String title;
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private UserShortDto initiator;
    private boolean paid;
    private int confirmedRequests;
    private int views;
}
