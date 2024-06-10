package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private CommentDto commentDto;
    private Comment comment;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);
        item.setUser(user);

        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Test Comment");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test Comment");
        comment.setItem(item);
        comment.setAuthor(user);
    }

    @Test
    public void testAddItemSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.addItem(itemDto, user.getId());

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testAddItemUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemService.addItem(itemDto, user.getId());
        });

        assertEquals("Пользователь не существует с таким id 1", exception.getMessage());
    }

    @Test
    public void testGetItemByIdSuccess() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(bookingRepository.existsByItemIdAndOwnerId(item.getId(), user.getId())).thenReturn(true);

        ItemDto result = itemService.getItemById(item.getId(), user.getId());

        assertNotNull(result);
        assertEquals(item.getName(), result.getName());
    }

    @Test
    public void testGetItemByIdItemNotFound() {
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemService.getItemById(item.getId(), user.getId());
        });

        assertEquals("Предмет с этим id 1 не существует", exception.getMessage());
    }

    @Test
    public void testUpdateItemSuccess() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        ItemDto result = itemService.updateItem(itemDto, item.getId(), user.getId());

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    public void testUpdateItemUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemService.updateItem(itemDto, item.getId(), user.getId());
        });

        assertEquals("Пользователь не существует с таким id 1", exception.getMessage());
    }

    @Test
    public void testDeleteItemByIdSuccess() {
        doNothing().when(itemRepository).deleteById(item.getId());

        itemService.deleteItemById(item.getId());

        verify(itemRepository, times(1)).deleteById(item.getId());
    }

    @Test
    public void testGetAllItemsByUserIdSuccess() {
        Page<Item> itemsPage = new PageImpl<>(Collections.singletonList(item));
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.findAllByUserIdOrderByBookingStartDesc(eq(user.getId()), any(PageRequest.class)))
                .thenReturn(itemsPage);

        List<ItemDto> result = itemService.getAllItemsByUserId(user.getId(), 0, 10);

        assertFalse(result.isEmpty());
        assertEquals(item.getId(), result.get(0).getId());
    }

    @Test
    public void testSearchByNameOrDescriptionSuccess() {
        Page<Item> itemsPage = new PageImpl<>(Collections.singletonList(item));
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.findByNameOrDescriptionContainingIgnoreCaseAndAvailable(anyString(), any(PageRequest.class)))
                .thenReturn(itemsPage);

        List<ItemDto> result = itemService.searchByNameOrDescription("Test", user.getId(), 0, 10);

        assertFalse(result.isEmpty());
        assertEquals(item.getId(), result.get(0).getId());
    }

    @Test
    public void testCreateCommentSuccess() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(bookingRepository.existsByItemIdAndBookerIdExcludingRejectedAndPast(item.getId(), user.getId())).thenReturn(true);
        when(bookingRepository.existsByBookerIdAndItemIdAndTimeStatusPastOrCurrent(user.getId(), item.getId())).thenReturn(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto result = itemService.createComment(item.getId(), user.getId(), commentDto);

        assertNotNull(result);
        assertEquals(commentDto.getText(), result.getText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    public void testCreateCommentUserNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(false);

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemService.createComment(item.getId(), user.getId(), commentDto);
        });

        assertEquals("Пользователь не существует с таким id 1", exception.getMessage());
    }

    @Test
    public void testCreateCommentItemNotFound() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(false);

        NotExistException exception = assertThrows(NotExistException.class, () -> {
            itemService.createComment(item.getId(), user.getId(), commentDto);
        });

        assertEquals("Предмет не существует с таким id 1", exception.getMessage());
    }

    @Test
    public void testCreateCommentNotEligible() {
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(itemRepository.existsById(item.getId())).thenReturn(true);
        when(bookingRepository.existsByItemIdAndBookerIdExcludingRejectedAndPast(item.getId(), user.getId())).thenReturn(false);

        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> {
            itemService.createComment(item.getId(), user.getId(), commentDto);
        });

        assertEquals("Вы не можете оставлять комментарии", exception.getMessage());
    }
}
