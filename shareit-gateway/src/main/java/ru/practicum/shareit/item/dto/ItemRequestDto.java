package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.annotation.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    @NotNull(groups = {Update.class})
    private long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private Boolean available;
    @NotNull
    private String description;
    private Long requestId;
}
