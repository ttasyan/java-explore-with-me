package ru.practicum.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid NewCategoryRequest request) {
        return service.addCategory(request);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto modifyCategory(@PathVariable long catId, @RequestBody NewCategoryRequest request) {
        return service.modifyCategory(catId, request);
    }
}
