package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.Event;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    private String title;
    private boolean pinned;
    private Set<Event> events;
}
