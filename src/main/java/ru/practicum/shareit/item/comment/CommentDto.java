package ru.practicum.shareit.item.comment;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.annotation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull
    @NotBlank
    private String text;
    private String authorName;
    private LocalDateTime created;
}
