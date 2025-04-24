package ru.practicum.compilation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilation {
    private Set<Long> events;
    @Builder.Default
    private Boolean pinned = false;
    @NotBlank
    @NotNull
    @Size(min = 1, max = 50)
    private String title;
}
