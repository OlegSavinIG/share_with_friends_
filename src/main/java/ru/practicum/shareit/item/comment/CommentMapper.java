package ru.practicum.shareit.item.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {
    public static ru.practicum.shareit.item.comment.CommentDto mapToCommentDto(ru.practicum.shareit.item.comment.Comment comment) {
        return ru.practicum.shareit.item.comment.CommentDto.builder()
                .authorName(comment.getAuthor().getName())
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreateTime())
                .build();
    }

    public static ru.practicum.shareit.item.comment.Comment mapToComment(CommentDto commentDto, User user, Item item) {
        return Comment.builder()
                .author(user)
                .createTime(LocalDateTime.now())
                .text(commentDto.getText())
                .item(item)
                .build();
    }
}
