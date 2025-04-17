package ru.practicum.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
public class AdminCompController {
    private final CompService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addComp(@RequestBody @Valid NewCompilation newCompilation) {
        return service.addComp(newCompilation);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComp(@PathVariable long compId) {
        service.deleteComp(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto modifyComp(@PathVariable long compId, @RequestBody @Valid NewCompilation newCompilation) {
        return service.modifyComp(compId, newCompilation);
    }

}
