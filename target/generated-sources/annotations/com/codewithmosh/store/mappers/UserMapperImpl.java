package com.codewithmosh.store.mappers;

import com.codewithmosh.store.controllers.AuthController;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T21:48:17+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.8 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;

        id = user.getId();
        name = user.getName();
        email = user.getEmail();

        UserDto userDto = new UserDto( id, name, email );

        return userDto;
    }

    @Override
    public User toEntity(RegisterUserRequest registerUserRequest) {
        if ( registerUserRequest == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.name( registerUserRequest.getName() );
        user.email( registerUserRequest.getEmail() );
        user.password( registerUserRequest.getPassword() );

        return user.build();
    }

    @Override
    public void update(UpdateUserDto updateUserRequest, User user) {
        if ( updateUserRequest == null ) {
            return;
        }

        user.setName( updateUserRequest.getName() );
        user.setEmail( updateUserRequest.getEmail() );
    }

    @Override
    public UserDto toDto(AuthController user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;

        UserDto userDto = new UserDto( id, name, email );

        return userDto;
    }
}
