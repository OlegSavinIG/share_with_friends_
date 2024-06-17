package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.item.model.ItemDtoWithRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Builder
public class ItemRequestDto {
    private String description;
    private LocalDateTime created;
    private final List<ItemDtoWithRequest> items = new ArrayList<>();
}
