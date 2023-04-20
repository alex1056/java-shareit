package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findItemRequestByRequesterIdOrderByCreatedDesc(Long requesterId);

    @Query(value = "select * from REQUESTS ir " +
            "where ir.requester_id <> ?1 "
            +
            "order by ir.created DESC ",
            nativeQuery = true
    )
    List<ItemRequest> findItemRequestPages(Long ownerId, PageRequest pageRequest);
}
