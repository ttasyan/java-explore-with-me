package ru.practicum.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {
   @Mapping(target = "id", ignore = true)
   User requestToUser(NewUserRequest request);

   UserDto userToUserDto(User user);
}
