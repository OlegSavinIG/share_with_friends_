package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.booking.model.BookingResponseWithItem;
import ru.practicum.shareit.item.comment.CommentDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    @NotNull(groups = {Update.class})
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Boolean available;
    @NotNull
    private String description;
    private String review;
    private BookingResponseWithItem lastBooking;
    private BookingResponseWithItem nextBooking;
    private List<CommentDto> comments;
}
