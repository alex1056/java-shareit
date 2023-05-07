package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "request_id")
    private Long requestId;
    @Transient
    private BookingToFrontDto lastBooking;
    @Transient
    private BookingToFrontDto nextBooking;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
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
