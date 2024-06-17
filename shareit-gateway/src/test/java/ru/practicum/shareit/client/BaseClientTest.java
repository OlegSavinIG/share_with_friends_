package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BaseClientTest {

    @Mock
    private RestTemplate restTemplate;

    private BaseClient baseClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        baseClient = new BaseClient(restTemplate);
    }

    @Test
    void testGet() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), anyMap())).thenReturn(response);

        ResponseEntity<Object> result = baseClient.get("/test", 1L, Map.of("param", "value"));

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/test"), eq(HttpMethod.GET), entityCaptor.capture(), eq(Object.class), eq(Map.of("param", "value")));
        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("1", headers.getFirst("X-Sharer-User-Id"));
    }

    @Test
    void testPost() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.CREATED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        Object requestBody = new Object();
        ResponseEntity<Object> result = baseClient.post("/test", 1L, requestBody);

        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/test"), eq(HttpMethod.POST), entityCaptor.capture(), eq(Object.class));
        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("1", headers.getFirst("X-Sharer-User-Id"));
    }

    @Test
    void testPut() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        Object requestBody = new Object();
        ResponseEntity<Object> result = baseClient.put("/test", 1L, requestBody);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/test"), eq(HttpMethod.PUT), entityCaptor.capture(), eq(Object.class));
        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("1", headers.getFirst("X-Sharer-User-Id"));
    }

    @Test
    void testPatch() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        Object requestBody = new Object();
        ResponseEntity<Object> result = baseClient.patch("/test", 1L, requestBody);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/test"), eq(HttpMethod.PATCH), entityCaptor.capture(), eq(Object.class));
        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("1", headers.getFirst("X-Sharer-User-Id"));
    }

    @Test
    void testDelete() {
        ResponseEntity<Object> response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class))).thenReturn(response);

        ResponseEntity<Object> result = baseClient.delete("/test", 1L);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        ArgumentCaptor<HttpEntity> entityCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        verify(restTemplate).exchange(eq("/test"), eq(HttpMethod.DELETE), entityCaptor.capture(), eq(Object.class));
        HttpHeaders headers = entityCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals("1", headers.getFirst("X-Sharer-User-Id"));
    }
}
