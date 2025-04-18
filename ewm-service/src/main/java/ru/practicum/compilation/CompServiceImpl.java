package ru.practicum.compilation;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.IntegrityConstraintViolationException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompServiceImpl implements CompService {
    private EventRepository eventRepository;
    private CompRepository repository;
    private CompMapper mapper;

    public CompilationDto addComp(NewCompilation newCompilation) {
        try {
            Compilation compilation = new Compilation();
            if (newCompilation.getPinned() != null) {
                compilation.setPinned(newCompilation.getPinned());
            } else {
                compilation.setPinned(false);
            }
            compilation.setTitle(newCompilation.getTitle());
            compilation.setEvents(newCompilation.getEvents().stream().map(event -> eventRepository.findById(event)
                            .orElseThrow(() -> new NotFoundException("Event with id=" + event + " not found")))
                    .collect(Collectors.toSet()));
            return mapper.complicationToComplicationDto(repository.save(compilation));
        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public void deleteComp(long compId) {
        findById(compId);
        repository.deleteById(compId);
    }

    public CompilationDto modifyComp(long compId, UpdateCompilationRequest newCompilation) {
        try {
            Compilation compilation = findById(compId);
            if (!newCompilation.getEvents().isEmpty()) {
                compilation.setEvents(newCompilation.getEvents().stream().map(event -> eventRepository.findById(event)
                                .orElseThrow(() -> new NotFoundException("Event with id=" + event + " not found")))
                        .collect(Collectors.toSet()));
            }
            if (newCompilation.getPinned() != null) {
                compilation.setPinned(newCompilation.getPinned());
            }
            if (!newCompilation.getTitle().isEmpty() || !newCompilation.getTitle().isBlank()) {
                compilation.setTitle(newCompilation.getTitle());
            }
            return mapper.complicationToComplicationDto(repository.save(compilation));
        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public List<CompilationDto> getAllPublic(boolean pinned, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        Page<Compilation> categories;
        if (pinned) {
            categories = repository.findByPinned(true, pageable);
        } else {
            categories = repository.findAll(pageable);
        }
        return categories.stream().map(category -> mapper.complicationToComplicationDto(category)).toList();

    }

    public CompilationDto getByIdPublic(long compId) {
        return mapper.complicationToComplicationDto(findById(compId));
    }

    private Compilation findById(long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " not found"));
    }
}
