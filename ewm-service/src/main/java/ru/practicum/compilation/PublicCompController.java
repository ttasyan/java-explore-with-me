package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompController {
    private final CompService service;

    @GetMapping
    public List<CompilationDto> getAllPublic(@RequestParam(required = false) boolean pinned,
                                             @RequestParam(required = false, defaultValue = "0") int from,
                                             @RequestParam(required = false, defaultValue = "10") int size) {
        return service.getAllPublic(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getByIdPublic(@PathVariable long compId) {
        return service.getByIdPublic(compId);
    }
}
