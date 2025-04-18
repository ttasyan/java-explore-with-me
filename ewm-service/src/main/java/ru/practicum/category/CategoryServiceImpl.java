package ru.practicum.category;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.AlreadyExistsException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper mapper;
    private final CategoryRepository repository;
    private EventRepository eventRepository;

    public CategoryDto addCategory(NewCategoryRequest request) {
        try {
            Category category = new Category();
            category.setName(request.getName());
            return mapper.categoryToCategoryDto(repository.save(category));
        } catch (ConstraintViolationException e) {
            throw isUnique(e, request.getName());
        }
    }

    public void deleteCategory(long catId) {
        findCategoryById(catId);
//        if (!eventRepository.findByCategoryId(catId).isEmpty()) {
//            throw new
//        }
        repository.deleteById(catId);
    }

    public CategoryDto modifyCategory(long catId, NewCategoryRequest request) {
        try {
            Category category = findCategoryById(catId);

            if (request.getName().length() > 50) {
                throw new ValidationException("Name length can not be > 50");
            }
            category.setName(request.getName());
            return mapper.categoryToCategoryDto(repository.save(category));
        } catch (ConstraintViolationException e) {
            throw isUnique(e, request.getName());
        }

    }

    public CategoryDto getById(long catId) {
        return mapper.categoryToCategoryDto(findCategoryById(catId));
    }

    public List<CategoryDto> getAll(int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categories = repository.findAll(pageable);
        return categories.stream().map(mapper::categoryToCategoryDto).toList();

    }

    private Category findCategoryById(long catId) {
        return repository.findById(catId).orElseThrow(() -> new NotFoundException("Category with id=" + catId + " nto found"));
    }

    private RuntimeException isUnique(RuntimeException e, String categoryName) {
        if (e.getMessage().contains("categories_name_key")) {
            return new AlreadyExistsException("Category with name " + categoryName + " already exists");
        }
        return new ValidationException("Field name is not correct");
    }
}
