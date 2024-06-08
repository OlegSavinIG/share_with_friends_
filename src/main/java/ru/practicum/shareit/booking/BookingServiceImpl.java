package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.*;
import ru.practicum.shareit.exception.BookingException;
import ru.practicum.shareit.exception.DataNotFoundException;
import ru.practicum.shareit.exception.NotExistException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;


    @Override
    public BookingResponse createBooking(Long bookerId, BookingDto bookingDto) {
        bookingValidator(bookingDto);
        boolean renterExist = userRepository.existsById(bookerId);
        if (!renterExist) {
            throw new NotExistException("Не найден пользователь");
        }
        Item item = itemRepository.findById(bookingDto.getItemId()).get();
        if (item.getUser().getId() == bookerId) {
            throw new BookingException("Владелец не может бронировать свою вещь");
        }
        User owner = userRepository.findById(item.getUser().getId()).get();
        User booker = userRepository.findById(bookerId).get();
        Booking booking = BookingMapper.mapToBooking(item, booker, owner, bookingDto);
        bookingRepository.save(booking);
        item.getBookings().add(booking);
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse approveBooking(Long bookingId, String approved, Long ownerId) {
        bookingApproveValidator(ownerId, bookingId);
        Booking booking = bookingRepository.findById(bookingId).get();
        booking.setStatus(approved.equals("true") ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse findById(Long bookingId, Long userId) {
        verifyUserExists(userId);
        boolean existsBookingWithUserAsBooker = bookingRepository.existsByBookerId(userId);
        boolean existsBookingWithUserAsOwner = bookingRepository.existsByOwnerId(userId);
        if (!existsBookingWithUserAsBooker && !existsBookingWithUserAsOwner) {
            throw new NotExistException("У пользователя с id %d нет бронирований", userId);
        }
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotExistException("Не существует бронирование с id %d", bookingId));
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByBooker(Long userId, String state) {
        verifyUserExists(userId);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
        return filterBookingsByState(bookings, state);
    }


    @Override
    public List<BookingResponse> getBookingsByOwner(Long userId, String state) {
        verifyUserExists(userId);
        List<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId);
        return filterBookingsByState(bookings, state);
    }

    @Override
    public List<BookingResponse> allBookingsByBooker(Long bookerId) {
        verifyUserExists(bookerId);
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> allBookingsByOwner(Long userId) {
        verifyUserExists(userId);
        List<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId);
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    private List<BookingResponse> filterBookingsByState(List<Booking> bookings, String state) {
        return bookings.stream()
                .peek(BookingStatusChecker::setBookingTimeStatus)
                .filter(booking -> matchState(booking, state))
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    private boolean matchState(Booking booking, String state) {
        switch (state) {
            case "ALL":
                return true;
            case "CURRENT":
                return booking.getTimeStatus().equals(BookingStatus.CURRENT);
            case "PAST":
                return booking.getTimeStatus().equals(BookingStatus.PAST);
            case "FUTURE":
                return booking.getTimeStatus().equals(BookingStatus.FUTURE);
            case "WAITING":
                return booking.getStatus().equals(BookingStatus.WAITING);
            case "REJECTED":
                return booking.getStatus().equals(BookingStatus.REJECTED);
            default:
                throw new BookingException("Unknown state: " + state);
        }
    }

    private void bookingValidator(BookingDto bookingDto) {
        boolean itemExist = itemRepository.existsById(bookingDto.getItemId());
        if (!itemExist) {
            throw new NotExistException("Не найден предмет");
        }
        boolean isAvailable = itemExist && itemRepository.findAvailableById(bookingDto.getItemId());
        if (!isAvailable) {
            throw new DataNotFoundException("Предмет не доступен для аренды");
        }
        boolean isBefore = bookingDto.getStart().isBefore(bookingDto.getEnd());
        if (!isBefore) {
            throw new DataNotFoundException("Неправильно указано начало аренды");
        }
        boolean isAfter = bookingDto.getStart().isAfter(LocalDateTime.now());
        if (!isAfter) {
            throw new DataNotFoundException("Неправильно указана дата аренды");
        }
    }

    private void verifyUserExists(Long userId) {
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
    }

    private void bookingApproveValidator(Long userId, Long bookingId) {
        boolean userExist = userRepository.existsById(userId);
        boolean bookingExist = bookingRepository.existsById(bookingId);
        if (!userExist) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
        if (!bookingExist) {
            throw new NotExistException("Не существует бронирование с id %d", bookingId);
        }
        Booking booking = bookingRepository.findById(bookingId).get();
        if (booking.getBooker().getId() == userId) {
            throw new BookingException("Вам не возможно изменить бронирование");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new DataNotFoundException("Бронирование уже одобрено");
        }
    }
}