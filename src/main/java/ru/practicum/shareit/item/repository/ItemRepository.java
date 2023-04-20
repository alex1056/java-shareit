package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemByOwnerId(Long userId, PageRequest pageRequest);

    Optional<Item> findItemByOwnerIdAndId(Long ownerId, Long id);

    @Query(" select i from Item i " +
            "where i.available=true AND (upper(i.name) like upper(concat('%', ?1, '%')) "
            +
            " or upper(i.description) like upper(concat('%', ?1, '%')))"
    )
    List<Item> search(String text, PageRequest pageRequest);

}
