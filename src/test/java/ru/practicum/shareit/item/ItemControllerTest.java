package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemDto = ItemDto.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("Test Comment")
                .authorName("Test Author")
                .build();
    }

    @Test
    void testCreateItem() throws Exception {
        given(itemService.addItem(any(ItemDto.class), anyLong())).willReturn(itemDto);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void testGetItemById() throws Exception {
        given(itemService.getItemById(anyLong(), anyLong())).willReturn(itemDto);

        mockMvc.perform(get("/items/{id}", 1L)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void testUpdateItem() throws Exception {
        given(itemService.updateItem(any(ItemDto.class), anyLong(), anyLong())).willReturn(itemDto);

        mockMvc.perform(patch("/items/{id}", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$.available").value(itemDto.getAvailable()));
    }

    @Test
    void testDeleteItem() throws Exception {
        doNothing().when(itemService).deleteItemById(anyLong());

        mockMvc.perform(delete("/items/{id}", 1L)
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllItemsByUserId() throws Exception {
        List<ItemDto> items = Collections.singletonList(itemDto);
        given(itemService.getAllItemsByUserId(anyLong(), anyInt(), anyInt())).willReturn(items);

        mockMvc.perform(get("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()));
    }

    @Test
    void testSearchByNameOrDescription() throws Exception {
        List<ItemDto> items = Collections.singletonList(itemDto);
        given(itemService.searchByNameOrDescription(anyString(), anyLong(), anyInt(), anyInt())).willReturn(items);

        mockMvc.perform(get("/items/search")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "Test")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()))
                .andExpect(jsonPath("$[0].available").value(itemDto.getAvailable()));
    }

    @Test
    void testCreateComment() throws Exception {
        given(itemService.createComment(anyLong(), anyLong(), any(CommentDto.class))).willReturn(commentDto);

        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));
    }
}
