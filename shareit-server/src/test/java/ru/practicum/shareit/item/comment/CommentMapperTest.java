package ru.practicum.shareit.item.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
public class CommentMapperTest {

    private User user;
    private Item item;
    private Comment comment;
    private CommentDto commentDto;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        item = Item.builder()
                .id(1L)
                .name("Test Item")
                .description("Test Description")
                .available(true)
                .user(user)
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("Test Comment")
                .createTime(LocalDateTime.now())
                .author(user)
                .item(item)
                .build();

        commentDto = CommentDto.builder()
                .id(1L)
                .text("Test Comment")
                .authorName("John Doe")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    public void shouldMapToCommentDtoCorrectly() {
        CommentDto mappedCommentDto = CommentMapper.mapToCommentDto(comment);

        assertEquals(comment.getId(), mappedCommentDto.getId());
        assertEquals(comment.getText(), mappedCommentDto.getText());
        assertEquals(comment.getAuthor().getName(), mappedCommentDto.getAuthorName());
        assertEquals(comment.getCreateTime(), mappedCommentDto.getCreated());
    }

    @Test
    public void shouldMapToCommentCorrectly() {
        Comment mappedComment = CommentMapper.mapToComment(commentDto, user, item);

        assertEquals(commentDto.getText(), mappedComment.getText());
        assertEquals(commentDto.getAuthorName(), mappedComment.getAuthor().getName());
        assertEquals(item.getId(), mappedComment.getItem().getId());
    }
}
