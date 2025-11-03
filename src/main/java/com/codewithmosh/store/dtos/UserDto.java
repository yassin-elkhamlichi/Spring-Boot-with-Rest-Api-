package com.codewithmosh.store.dtos;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;

    @Override
    public String toString() {
        return "{\n" +
                "  \"email\": \"" + getEmail() + "\",\n" +
                "  \"name\": \"" + getName() + "\",\n" +
                "  \"id\": \"" + getId() + "\"\n" +
                "}";
    }
}
