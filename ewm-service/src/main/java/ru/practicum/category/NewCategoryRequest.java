package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewCategoryRequest {
    private String name;
}
