package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ChangePasswordReqeust;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UpdateUserDto;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Set;


@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestParam(required = false,  defaultValue = "" , name = "sort") String sortBy) {
        if(!Set.of("email","name").contains(sortBy))
            sortBy = "name";
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        var user =  userRepository.findById(id).orElse(null);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
       return ResponseEntity.ok(userMapper.toDto(user));
    }
    @PostMapping
    public UserDto createUser(@RequestBody RegisterUserRequest data) {
        var user = userMapper.toEntity(data);
        userRepository.save(user);
        var userDto = userMapper.toDto(user);
        return userDto;
    }
    @PutMapping("/{id}")
    public  ResponseEntity updateUser(
        @PathVariable(name = "id") Long id,
        @RequestBody UpdateUserDto data
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userMapper.update(data,user);
        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(
            @PathVariable(name = "id") Long id
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/Change_password")
    public ResponseEntity changePassword(
            @PathVariable(name = "id") Long id,
            @RequestBody ChangePasswordReqeust data
    ){
        var user = userRepository.findById(id).orElse(null);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        if(!user.getPassword().equals(data.getOldPassword())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        user.setPassword(data.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}
