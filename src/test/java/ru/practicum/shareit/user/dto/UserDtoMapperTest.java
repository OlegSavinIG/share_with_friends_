package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class UserDtoMapperTest {

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();
    }

    @Test
    public void shouldMapUserToUserDtoCorrectly() {
        UserDto mappedUserDto = UserDtoMapper.userToUserDto(user);

        assertEquals(user.getId(), mappedUserDto.getId());
        assertEquals(user.getName(), mappedUserDto.getName());
        assertEquals(user.getEmail(), mappedUserDto.getEmail());
    }

    @Test
    public void shouldMapUserDtoToUserCorrectly() {
        User mappedUser = UserDtoMapper.userDtoToUser(userDto);

        assertEquals(userDto.getId(), mappedUser.getId());
        assertEquals(userDto.getName(), mappedUser.getName());
        assertEquals(userDto.getEmail(), mappedUser.getEmail());
    }
}
