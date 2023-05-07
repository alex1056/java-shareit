package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingFromFrontDto {
    private Long id;
    private LocalDateTime start; // YYYY-MM-DDTHH:mm:ss
    private LocalDateTime end;

    private Long itemId;

    private Long bookerId;
    private BookingStatus status;
}
