package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private UserClient userClient;

    private static final String SERVER_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(builder.uriTemplateHandler(any(DefaultUriBuilderFactory.class))).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        userClient = new UserClient(SERVER_URL, builder);
    }

    @Test
    void testCreateUser() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        UserRequestDto userRequestDto = new UserRequestDto();
        ResponseEntity<Object> result = userClient.createUser(userRequestDto);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testGetUserById() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = userClient.getUserById(1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testUpdateUser() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        UserRequestDto userRequestDto = new UserRequestDto();
        ResponseEntity<Object> result = userClient.updateUser(userRequestDto, 1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1"),
                eq(HttpMethod.PATCH),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testDeleteById() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = userClient.deleteById(1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testGetAllUsers() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = userClient.getAllUsers();

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }
}
