package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto createBooking(Long bookerId, BookingDto bookingDto) {
        boolean renterExist = userRepository.existsById(bookerId);
        boolean itemExist = itemRepository.existsById(bookingDto.getItemId());
        if (renterExist && itemExist) {
            Item item = itemRepository.findById(bookingDto.getItemId()).get();
            User owner = userRepository.findById(item.getUser().getId()).get();
            User renter = userRepository.findById(bookerId).get();
            Booking booking = BookingMapper.mapToBooking(item, renter, owner, bookingDto);
            bookingRepository.save(booking);
            return BookingMapper.mapToBookingDto(booking);
        } else throw new NotExistException("Не существует пользователь или предмет");
    }

    @Override
    public BookingDto approveBooking(Long bookingId, String approved, Long userId) {
        boolean userExist = userRepository.existsById(userId);
        boolean bookingExist = bookingRepository.existsById(bookingId);
        if (!userExist) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
        if (!bookingExist) {
            throw new NotExistException("Не существует бронирование с id %d", bookingId);
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (approved.equals("true")) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingDto(booking);
    }

    @Override
    public BookingDto findById(Long bookingId, Long userId) {
        boolean existsById = userRepository.existsById(userId);
        if (existsById) {
            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new NotExistException("Не существует бронирование с id %d", bookingId));
            return BookingMapper.mapToBookingDto(booking);
        } else throw new NotExistException("Не существует пользователь с id %d", userId);
    }

    @Override
    public List<BookingDto> allBookingsByBooker(String state, Long bookerId) {
        boolean bookerExist = userRepository.existsById(bookerId);
        if (!bookerExist) {
            throw new NotExistException("Не существует пользователь с id %d", bookerId);
        }
        List<Booking> bookings = bookingRepository.findAllByBookerId(bookerId);
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> allBookingsByOwner(String state, Long ownerId) {
        boolean bookerExist = userRepository.existsById(ownerId);
        if (!bookerExist) {
            throw new NotExistException("Не существует пользователь с id %d", ownerId);
        }
        List<Booking> bookings = bookingRepository.findAllByOwnerId(ownerId);
        return bookings.stream()
                .map(BookingMapper::mapToBookingDto)
                .collect(Collectors.toList());
    }
}
