package ru.practicum.user;


import java.util.List;

public interface UserService {
    List<UserDto> getAll(List<Long> ids, int from, int size);

    UserDto addUser(NewUserRequest request);

    void delete(Long userId);
}
