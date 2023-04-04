package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByBookerIdOrderByStartDesc(Long bookerId);

    Optional<Booking> findBookingByIdAndBookerId(Long id, Long bookerId);

    List<Booking> findBookingByItemIdAndBookerId(Long itemId, Long bookerId);

    @Query(value = "select * from bookings b " +
            "where b.id = ?1 " +
            "AND ( " +
            "b.booker_id = ?2 " +
            "or " +
            "?2 in (" +
            "select i.owner_id from items as i " +
            "where b.item_id = i.id AND ?2 = i.owner_id ) " +
            ") " +
            "order by b.creation_date desc",
            nativeQuery = true
    )
    Optional<Booking> findBooking(Long bookingId, Long userId);

    @Query(value = "select * from bookings b " +
            "where b.item_id IN " +
            "( " +
            "select i.id from items as i " +
            "where b.item_id = i.id AND ?1 = i.owner_id " +
            ") " +
            "order by b.creation_date desc",
            nativeQuery = true
    )
    List<Booking> findBookingsByOwner(Long userId);

    @Query(value = "select * from bookings b " +
            "where b.id = ?1 " +
            "AND ( " +
            "?2 in (" +
            "select i.owner_id from items as i " +
            "where b.item_id = i.id AND ?2 = i.owner_id ) " +
            ") " +
            "order by b.creation_date desc",
            nativeQuery = true
    )
    Optional<Booking> findBookingForApprove(Long bookingId, Long userId);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "AND " +
            "b.end_date <= ?2 " +
            "order by b.end_date desc " +
            "limit 1 ",
            nativeQuery = true
    )
    Optional<Booking> findNearestBookingByEndDate(Long itemId, LocalDateTime dateNow);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "AND " +
            "b.booker_id = ?2 " +
            "AND " +
            "b.end_date <= ?3 " +
            "order by b.end_date desc " +
            "limit 1 ",
            nativeQuery = true
    )
    Optional<Booking> findNearestBookingByEndDateAndUserId(Long itemId, Long userId, LocalDateTime dateNow);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "AND " +
            "b.start_date >= ?2 " +
            "order by b.start_date asc " +
            "limit 1 ",
            nativeQuery = true
    )
    Optional<Booking> findNearestBookingByStartDate(Long itemId, LocalDateTime dateNow);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "AND " +
            "b.booker_id = ?2 " +
            "AND " +
            "b.start_date >= ?3 " +
            "order by b.start_date asc " +
            "limit 1 ",
            nativeQuery = true
    )
    Optional<Booking> findNearestBookingByStartDateAndUserId(Long itemId, Long userId, LocalDateTime dateNow);


    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "AND " +
            "b.start_date <= ?2 AND b.end_date >= ?2 " +
            "order by b.end_date desc " +
            "limit 1 ",
            nativeQuery = true
    )
    Optional<Booking> findNearestBookingByCurrentDate(Long itemId, LocalDateTime dateNow);
}
