package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ItemClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private ItemClient itemClient;

    private static final String SERVER_URL = "http://localhost:8080";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(builder.uriTemplateHandler(any(DefaultUriBuilderFactory.class))).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        itemClient = new ItemClient(SERVER_URL, builder);
    }

    @Test
    void testCreateItem() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ItemRequestDto itemDto = new ItemRequestDto();
        ResponseEntity<Object> result = itemClient.createItem(itemDto, 1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq(""),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testGetItem() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = itemClient.getItem(1L, 1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testUpdateItem() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ItemRequestDto itemDto = new ItemRequestDto();
        ResponseEntity<Object> result = itemClient.updateItem(itemDto, 1L, 1L);

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

        ResponseEntity<Object> result = itemClient.deleteById(1L);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }

    @Test
    void testGetAllItems() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), anyMap())).thenReturn(response);

        ResponseEntity<Object> result = itemClient.getAllItems(1L, 0, 10);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("?from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("from", 0, "size", 10))
        );
    }

    @Test
    void testSearchByNameOrDescription() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), anyMap())).thenReturn(response);

        ResponseEntity<Object> result = itemClient.searchByNameOrDescription("test", 1L, 0, 10);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/search?text={text}&from={from}&size={size}"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Object.class),
                eq(Map.of("text", "test", "from", 0, "size", 10))
        );
    }

    @Test
    void testCreateComment() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        CommentRequestDto commentDto = new CommentRequestDto();
        ResponseEntity<Object> result = itemClient.createComment(1L, 1L, commentDto);

        assertNotNull(result);
        verify(restTemplate).exchange(
                eq("/1/comment"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(Object.class)
        );
    }
}
