package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Test
    public void testSerialize() throws Exception {
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        assertThat(this.json.write(userDto))
                .hasJsonPathNumberValue("$.id")
                .extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(this.json.write(userDto))
                .hasJsonPathStringValue("$.name")
                .extractingJsonPathStringValue("$.name").isEqualTo("Test");
        assertThat(this.json.write(userDto))
                .hasJsonPathStringValue("$.email")
                .extractingJsonPathStringValue("$.email").isEqualTo("test@example.com");
    }

    @Test
    public void testDeserialize() throws Exception {
        String content = "{\"id\":1,\"name\":\"Test\",\"email\":\"test@example.com\"}";

        UserDto expectedUserDto = UserDto.builder()
                .id(1L)
                .name("Test")
                .email("test@example.com")
                .build();

        assertThat(this.json.parse(content)).isEqualTo(expectedUserDto);
        assertThat(this.json.parseObject(content).getId()).isEqualTo(1L);
        assertThat(this.json.parseObject(content).getName()).isEqualTo("Test");
        assertThat(this.json.parseObject(content).getEmail()).isEqualTo("test@example.com");
    }
}
