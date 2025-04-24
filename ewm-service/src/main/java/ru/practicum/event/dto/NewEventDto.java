package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewEventDto {
    @NotNull
    private Long category;
    @NotNull
    @Size(min = 3, max = 120)
    private String title;
    @NotNull @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime eventDate;
    @Builder.Default
    private Boolean paid = false;
    @NotNull @NotBlank
    @Size(min = 20, max = 7000)
    private String description;
    @NotNull
    private LocationDto location;
    @PositiveOrZero
    @Builder.Default
    private Integer participantLimit = 0;
    @Builder.Default
    private Boolean requestModeration = true;

}
