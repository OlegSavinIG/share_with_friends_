package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.UserDto;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDto = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .build();
    }

    @Test
    void testCreateUser() throws Exception {
        given(userService.addUser(any(UserDto.class))).willReturn(userDto);

        mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testGetUserById() throws Exception {
        given(userService.getUserById(anyLong())).willReturn(userDto);

        mockMvc.perform(get("/users/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        given(userService.getUserById(anyLong())).willReturn(null);

        mockMvc.perform(get("/users/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateUser() throws Exception {
        given(userService.updateUser(any(UserDto.class), anyLong())).willReturn(userDto);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.name").value(userDto.getName()))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()));
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        given(userService.updateUser(any(UserDto.class), anyLong())).willReturn(null);

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUserById(anyLong());

        mockMvc.perform(delete("/users/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDto> users = Collections.singletonList(userDto);
        given(userService.getAllUsers()).willReturn(users);

        mockMvc.perform(get("/users")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(userDto.getId()))
                .andExpect(jsonPath("$[0].name").value(userDto.getName()))
                .andExpect(jsonPath("$[0].email").value(userDto.getEmail()));
    }

//    @Test
//    void testCreateUserInvalidEmail() throws Exception {
//        UserDto invalidUser = UserDto.builder()
//                .id(1L)
//                .name("Test User")
//                .email("invalid-email")
//                .build();
//
//        mockMvc.perform(post("/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(invalidUser)))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    void testCreateUserMissingName() throws Exception {
//        UserDto invalidUser = UserDto.builder()
//                .id(1L)
//                .email("test@example.com")
//                .build();
//
//        mockMvc.perform(post("/users")
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(invalidUser)))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void testUpdateUserInvalidEmail() throws Exception {
        UserDto invalidUser = UserDto.builder()
                .id(1L)
                .name("Test User")
                .email("invalid-email")
                .build();

        mockMvc.perform(patch("/users/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isNotFound());
    }
}
