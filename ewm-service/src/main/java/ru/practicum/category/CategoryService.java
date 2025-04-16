package ru.practicum.category;


import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryRequest request);

    void deleteCategory(long catId);

    CategoryDto modifyCategory(long catId, NewCategoryRequest request);

    CategoryDto getById(long catId);

    List<CategoryDto> getAll(int from, int size);
}
