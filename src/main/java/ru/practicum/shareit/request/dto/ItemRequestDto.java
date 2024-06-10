package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.annotation.Update;
import ru.practicum.shareit.item.dto.ItemDtoWithRequest;

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
    @NotNull(groups = {Update.class})
    private Long id;
    @NotNull
    @NotBlank
    private String description;
    private LocalDateTime created;
    private final List<ItemDtoWithRequest> items = new ArrayList<>();
}
