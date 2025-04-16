package ru.practicum.compilation;


import java.util.List;

public interface CompService {
    CompilationDto addComp(NewCompilation newCompilation);

    void deleteComp(long compId);

    CompilationDto modifyComp(long compId, NewCompilation newCompilation);

    List<CompilationDto> getAllPublic(boolean pinned, int from, int size);

    CompilationDto getByIdPublic(long compId);
}
