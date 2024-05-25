package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b WHERE b.booker.id = :bookerId AND (:status IS NULL OR b.status = :status)")
    List<Booking> findAllByBookerIdAndStatus(@Param("bookerId") Long bookerId, @Param("status") BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.owner.id = :ownerId AND (:status IS NULL OR b.status = :status)")
    List<Booking> findAllByOwnerIdAndStatus(@Param("ownerId") Long ownerId, @Param("status") BookingStatus status);

    List<Booking> findAllByBookerId(Long bookerId);

    List<Booking> findAllByOwnerId(Long ownerId);
}

