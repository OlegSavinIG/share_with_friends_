package ru.practicum.shareit.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {


    boolean existsByItemIdAndOwnerId(Long itemId, Long ownerId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.item.id = :itemId AND b.booker.id = :bookerId " +
            "AND b.status NOT IN ('REJECTED', 'PAST', 'FUTURE')")
    boolean existsByItemIdAndBookerIdExcludingRejectedAndPast(@Param("itemId") Long itemId, @Param("bookerId") Long bookerId);


    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.booker.id = :bookerId AND b.item.id = :itemId AND (b.timeStatus = 'PAST' OR b.timeStatus = 'CURRENT')")
    boolean existsByBookerIdAndItemIdAndTimeStatusPastOrCurrent(@Param("bookerId") Long bookerId,
                                                                @Param("itemId") Long itemId);


    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId ORDER BY b.start DESC")
    Page<Booking> findAllByBookerIdOrderByStartDesc(@Param("bookerId") Long bookerId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId ORDER BY b.start DESC")
    Page<Booking> findAllByOwnerIdOrderByStartDesc(@Param("ownerId") Long ownerId, Pageable pageable);

}


