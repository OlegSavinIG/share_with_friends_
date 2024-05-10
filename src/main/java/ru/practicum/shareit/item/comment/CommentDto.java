package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
@Builder
public class CommentDto {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull
    private String text;
    private String authorName;
    private LocalDateTime createTime;
}
