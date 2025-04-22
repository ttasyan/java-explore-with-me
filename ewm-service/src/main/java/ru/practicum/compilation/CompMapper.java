package ru.practicum.compilation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.category.Category;

@Mapper
public interface CompMapper {
    @Mapping(target = "pinned", source = "pinned", defaultValue = "false")
    CompilationDto complicationToComplicationDto(Compilation compilation);

    default Category map(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }
}
