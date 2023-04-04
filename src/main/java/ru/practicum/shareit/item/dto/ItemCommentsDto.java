package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCommentsDto {
    private Long id;
    @NotNull(message = "У вещи должно быть название")
    @NotEmpty(message = "Название должно быть заполнено")
    private String name;
    @NotNull(message = "У вещи должно быть описание")
    private String description;
    @NotNull(message = "Укажите доступность для аренды вещи")
    private Boolean available;
    private Long requestId;
    private BookingToFrontDto lastBooking;
    private BookingToFrontDto nextBooking;
    private Set<CommentDto> comments;

    public Boolean isAvailable() {
        return available;
    }
}