package ru.practicum.user;

import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
   User requestToUser(NewUserRequest request);
   UserDto userToUserDto(User user);
}
