package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemCommentDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingToFrontDto lastBooking;
    private BookingToFrontDto nextBooking;
    private Set<CommentDto> comments;

    public Boolean isAvailable() {
        return available;
    }
}