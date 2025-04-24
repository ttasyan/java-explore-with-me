package ru.practicum.category;

import org.mapstruct.Mapper;

@Mapper
public interface CategoryMapper {
    CategoryDto categoryToCategoryDto(Category category);
}
