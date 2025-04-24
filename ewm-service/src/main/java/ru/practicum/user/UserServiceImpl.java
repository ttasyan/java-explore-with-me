package ru.practicum.user;

import jakarta.validation.ConstraintViolationException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.exception.DuplicatedDataException;
import ru.practicum.exception.IntegrityConstraintViolationException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository repository;
    private UserMapper mapper;

    public List<UserDto> getAll(List<Long> ids, int from, int size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<User> users;
        if (ids != null && !ids.isEmpty()) {
            users = repository.findUsersByIdIn(ids, pageable);
        } else {
            users = repository.findAll(pageable);
        }
        return users.stream().map(user -> mapper.userToUserDto(user)).toList();

    }

    public UserDto addUser(NewUserRequest request) {
        try {
            findByEmail(request.getEmail());
            return mapper.userToUserDto(repository.save(mapper.requestToUser(request)));
        } catch (ConstraintViolationException e) {
            throw new IntegrityConstraintViolationException("Нарушение ограничения");
        }
    }

    public void delete(Long userId) {
        if (repository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        repository.deleteById(userId);
    }

    private void findByEmail(String email) {
        if (repository.existsByEmail(email)) {
            throw new DuplicatedDataException("Email already in use");
        }
    }
}
