package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
    private BookingToFrontDto lastBooking;
    private BookingToFrontDto nextBooking;
    private Set<Comment> comments;

    public Boolean isAvailable() {
        return available;
    }

    public String getName() {
        return name != null ? name.trim() : name;
    }

    public String getDescription() {
        return description != null ? description.trim() : description;
    }
}
