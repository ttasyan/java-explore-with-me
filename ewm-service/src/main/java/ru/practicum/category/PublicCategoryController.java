package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final CategoryService service;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam int from, @PathVariable int size) {
        return service.getAll(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        return service.getById(catId);
    }
}
