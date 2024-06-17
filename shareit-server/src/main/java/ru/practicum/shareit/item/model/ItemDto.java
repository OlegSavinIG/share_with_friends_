package ru.practicum.shareit.item.model;

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
    private long id;
    private String name;
    private Boolean available;
    private String description;
    private Long requestId;
    private String review;
    private BookingResponseWithItem lastBooking;
    private BookingResponseWithItem nextBooking;
    private List<CommentDto> comments;
}
