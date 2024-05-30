package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND (:status IS NULL OR b.status = :status)")
    List<Booking> findAllByBookerIdAndStatus(@Param("bookerId") Long bookerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId AND (:status IS NULL OR b.status = :status)")
    List<Booking> findAllByOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") BookingStatus status);

    List<Booking> findAllByBookerId(Long bookerId);

    List<Booking> findAllByOwnerId(Long ownerId);

    boolean existsByBookerId(Long userId);

    boolean existsByOwnerId(Long userId);

    //    @Query("SELECT b FROM Booking b WHERE b.status NOT IN ('REJECTED', 'PAST')")
//    List<Booking> findAllByBookerIdExcludingRejectedAndPast(Long bookerId);
//
//    @Query("SELECT b FROM Booking b WHERE b.status NOT IN ('REJECTED', 'PAST')")
//    List<Booking> findAllByOwnerIdExcludingRejectedAndPast(Long ownerId);
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.status NOT IN ('REJECTED', 'PAST')")
    List<Booking> findAllByBookerIdExcludingRejectedAndPast(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId AND b.status NOT IN ('REJECTED', 'PAST')")
    List<Booking> findAllByOwnerIdExcludingRejectedAndPast(@Param("ownerId") Long ownerId);

    boolean existsByItemIdAndBookerId(Long itemId, Long bookerId);

    boolean existsByItemIdAndOwnerId(Long itemId, Long ownerId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.item.id = :itemId AND b.booker.id = :bookerId " +
            "AND b.status NOT IN ('REJECTED', 'PAST', 'FUTURE')")
    boolean existsByItemIdAndBookerIdExcludingRejectedAndPast(@Param("itemId") Long itemId, @Param("bookerId") Long bookerId);

    List<Booking> findAllByItemIdOrderByStartDesc(Long id);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId " +
            "AND b.start = (SELECT MAX(b2.start) FROM Booking b2 " +
            "WHERE b2.item.id = b.item.id AND b2.booker.id = :bookerId) " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByBookerIdSortedByStartGroupByItemId(@Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId " +
            "AND b.start = (SELECT MAX(b2.start) FROM Booking b2 " +
            "WHERE b2.item.id = b.item.id AND b2.owner.id = :ownerId) " +
            "ORDER BY b.start DESC")
    List<Booking> findAllByOwnerIdSortedByStartGroupByItemId(@Param("ownerId") Long ownerId);

    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND b.item.id = :itemId AND b.status = 'CURRENT'")
    Optional<Booking> findByBookerIdAndItemIdAndCurrentStatus(@Param("bookerId") Long bookerId, @Param("itemId") Long itemId);

    List<Booking> findAllByItemId(Long id);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.status NOT IN ('REJECTED', 'PAST')")
    List<Booking> findAllByItemIdExcludingRejectedAndPast(@Param("itemId") Long itemId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.item.id = :itemId AND b.booker.id = :bookerId AND b.status IN ('CURRENT', 'PAST')")
    boolean existsByItemIdAndBookerIdWithCurrentOrPastStatus(@Param("itemId") Long itemId, @Param("bookerId") Long bookerId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND (:status IS NULL OR b.status = :status) ORDER BY b.start")
    List<Booking> findAllByBookerIdAndStatusSortedByStart(@Param("bookerId") Long bookerId, @Param("status") BookingStatus status);
}


