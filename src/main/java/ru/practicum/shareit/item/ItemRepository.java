package ru.practicum.shareit.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) AND i.available = TRUE) " +
            "OR (LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%')) AND i.available = TRUE)")
    Page<Item> findByNameOrDescriptionContainingIgnoreCaseAndAvailable(@Param("text") String nameOrDescription, Pageable pageable);

    @Query("SELECT i.available FROM Item i WHERE i.id = :id")
    Boolean findAvailableById(@Param("id") long id);

    @Query("SELECT i FROM Item i LEFT JOIN i.bookings b ON b.item = i WHERE i.user.id = :userId " +
            "GROUP BY i.id ORDER BY MAX(b.start) NULLS LAST")
    Page<Item> findAllByUserIdOrderByBookingStartDesc(@Param("userId") Long userId, Pageable pageable);
}
