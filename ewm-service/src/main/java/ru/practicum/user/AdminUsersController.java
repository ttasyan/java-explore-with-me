package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUsersController {
    private final UserService service;

    @GetMapping
    public List<UserDto> getAll(@RequestParam(required = false, name = "ids") List<Long> ids,
                                @RequestParam(name = "from", required = false, defaultValue = "0") int from,
                                @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        return service.getAll(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody NewUserRequest request) {
        return service.addUser(request);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        service.delete(userId);
    }
}
