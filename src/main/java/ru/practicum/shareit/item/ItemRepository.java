package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByUserId(Long userId);
    List<Item> findByNameContainingIgnoreCaseAndAvailableIsTrueOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String text, String text2);
    @Query("SELECT i.available FROM Item i WHERE i.id = :id")
    Boolean findAvailableById(@Param("id") long id);
}
