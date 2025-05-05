package ru.practicum.event.mapper;

import org.mapstruct.*;
import ru.practicum.category.Category;
import ru.practicum.category.CategoryDto;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;

@Mapper
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "state", expression = "java(ru.practicum.event.model.EventStatus.PENDING)")
    @Mapping(target = "participantLimit", source = "participantLimit", defaultValue = "0")
    @Mapping(target = "paid", source = "paid", defaultValue = "false")
    @Mapping(target = "requestModeration", source = "requestModeration", defaultValue = "true")
    @Mapping(target = "category", source = "category")
    Event newEventToEvent(NewEventDto newEventDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "comments", expression = "java(new java.util.ArrayList<>())")
    EventFullDto eventToEventFullDto(Event event);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "eventDate", source = "eventDate", dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto eventToEventShortDto(Event event);

    @Mapping(target = "category", source = "categoryId")
    default Category map(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

    default CategoryDto map(Category c) {
        if (c == null) return null;
        CategoryDto dto = new CategoryDto();
        dto.setId(c.getId());
        dto.setName(c.getName());
        return dto;
    }
}
