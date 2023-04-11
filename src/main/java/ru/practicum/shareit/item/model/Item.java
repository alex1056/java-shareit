package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingToFrontDto;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

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


    //    @OneToMany(fetch = FetchType.EAGER)
//    @JoinColumn(name = "item_id")
//    private Set<Comment> comments;
//    @ElementCollection
//    @CollectionTable(name = "comments", joinColumns = @JoinColumn(name = "item_id"))
//    private Set<Comment> comments;

    public Boolean isAvailable() {
        return available;
    }

    public String getName() {
        if (name != null) return name.trim();
        return name;
    }

    public String getDescription() {
        if (description != null) return description.trim();
        return description;
    }

    public Set<Comment> getComments() {
        if (comments == null) return null;
        return comments.stream().map(comment -> {
            String text = comment.getText().trim();
            comment.setText(text);
            return comment;
        }).collect(Collectors.toSet());
    }
}
