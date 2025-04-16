package ru.practicum.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserRequest {
    private String name;
    private String email;
}
