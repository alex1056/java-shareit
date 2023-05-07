package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "requests", schema = "public")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    @Column(name = "requester_id")
    private Long requesterId;
    private LocalDateTime created;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_id")
    private Set<Item> items;

    public String getDescription() {
        return description != null ? description.trim() : description;
    }
}

