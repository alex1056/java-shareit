package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingFromFrontDto {
    private Long id;
    @NotNull
    private LocalDateTime start; // YYYY-MM-DDTHH:mm:ss
    @NotNull
    private LocalDateTime end;

    @NotNull
    private Long itemId;

    private Long bookerId;
    private BookingStatus status;
}
