package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingToFrontDto {
    private Long id;
    private LocalDateTime start; // YYYY-MM-DDTHH:mm:ss
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private Long bookerId;
    private BookingStatus status;
    private BookingState state;
}
