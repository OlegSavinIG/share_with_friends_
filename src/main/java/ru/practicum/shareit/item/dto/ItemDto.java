package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
   @NotNull(groups = {Update.class})
   private long id;
   @NotNull
   @NotBlank
   private String title;
   @NotNull
   private boolean isAvailable;
   @NotNull
   private String description;
   private String review;
}
