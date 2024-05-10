package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.*;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items", schema = "public")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private final TreeSet<Booking> bookings = new TreeSet<>(Comparator.comparing(Booking::getEnd));
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private final List<Comment> comments = new ArrayList<>();
}
