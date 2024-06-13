package ru.practicum.shareit.booking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.annotation.Update;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull
    private Long itemId;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    private BookingStatus status;
}
