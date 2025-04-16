package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class NewCompilation {
    private Set<Long> events;
    private Boolean pinned;
    private String title;
}
