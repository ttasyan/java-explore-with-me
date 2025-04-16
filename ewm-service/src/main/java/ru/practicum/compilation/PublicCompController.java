package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class PublicCompController {
    private CompService service;
    @GetMapping
    public List<CompilationDto> getAllPublic(@RequestParam boolean pinned, @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return service.getAllPublic(pinned, from, size);
    }
    @GetMapping("/{compId}")
    public CompilationDto getByIdPublic(@PathVariable long compId) {
        return service.getByIdPublic(compId);
    }
}
