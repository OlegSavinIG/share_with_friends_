package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.annotation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@SuperBuilder
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
}
