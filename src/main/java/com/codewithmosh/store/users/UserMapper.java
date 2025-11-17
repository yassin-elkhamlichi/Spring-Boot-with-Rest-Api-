package com.codewithmosh.store.users;


import com.codewithmosh.store.auth.AuthController;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest registerUserRequest);
    void update(UpdateUserDto updateUserRequest, @MappingTarget User user);
    UserDto toDto(AuthController user);
}
