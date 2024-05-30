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
//        ItemDto itemDto = ItemMapper.mapItemToItemDto(booking.getItem());
//        User booker = booking.getBooker();
//        Hibernate.initialize(itemDto);
//        Hibernate.initialize(booker);
        if (approved.equals("true")) {
            LocalDateTime start = booking.getStart();
            LocalDateTime end = booking.getEnd();
            LocalDateTime now = LocalDateTime.now();

            if (start.isBefore(now) && end.isAfter(now)) {
                booking.setStatus(BookingStatus.CURRENT);
            }
            if (end.isBefore(now)) {
                booking.setStatus(BookingStatus.PAST);
            }
            if (start.isAfter(now)) {
                booking.setStatus(BookingStatus.FUTURE);
            }
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse findById(Long bookingId, Long userId) {
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
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
    public List<BookingResponse> allBookingsByBookerAndStatus(String state, Long bookerId) {
        boolean bookerExist = userRepository.existsById(bookerId);
        if (!bookerExist) {
            throw new NotExistException("Не существует пользователь с id %d", bookerId);
        }
        if (state.equalsIgnoreCase("future")) {
            List<Booking> bookings = bookingRepository.findAllByBookerIdExcludingRejectedAndPast(bookerId);
            return bookings.stream()
                    .map(BookingMapper::mapToBookingResponse)
                    .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                    .collect(Collectors.toList());
        }
        if (state.equalsIgnoreCase("current")) {
            List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusSortedByStart(bookerId, BookingStatus.valueOf(state.toUpperCase()));
            return bookings.stream()
                    .map(BookingMapper::mapToBookingResponse)
                    .collect(Collectors.toList());
        }
        List<Booking> bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.valueOf(state));
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> allBookingsByBooker(Long bookerId) {
        boolean bookerExist = userRepository.existsById(bookerId);
        if (!bookerExist) {
            throw new NotExistException("Не существует пользователь с id %d", bookerId);
        }
        List<Booking> bookings = bookingRepository.findAllByBookerId(bookerId);
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingResponse> allBookingsByOwner(Long ownerId) {
        boolean bookerExist = userRepository.existsById(ownerId);
        if (!bookerExist) {
            throw new NotExistException("Не существует пользователь с id %d", ownerId);
        }
        List<Booking> bookings = bookingRepository.findAllByOwnerId(ownerId);
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> allBookingsByOwnerAndStatus(String state, Long ownerId) {
        boolean bookerExist = userRepository.existsById(ownerId);
        if (!bookerExist) {
            throw new NotExistException("Не существует пользователь с id %d", ownerId);
        }
        if (state.equalsIgnoreCase("future")) {
            List<Booking> bookings = bookingRepository.findAllByOwnerIdExcludingRejectedAndPast(ownerId);
            return bookings.stream()
                    .map(BookingMapper::mapToBookingResponse)
                    .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                    .collect(Collectors.toList());
        }
        if (state.equalsIgnoreCase("current")) {
            List<Booking> bookings = bookingRepository.findAllByOwnerIdSortedByStartGroupByItemId(ownerId);
            return bookings.stream()
                    .map(BookingMapper::mapToBookingResponse)
//                    .peek(bookingResponse -> bookingResponse.setStatus(BookingStatus.CURRENT))
                    .collect(Collectors.toList());
        }
        List<Booking> bookings = bookingRepository.findAllByOwnerIdAndStatus(ownerId, BookingStatus.valueOf(state));
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    public void bookingValidator(BookingDto bookingDto) {
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

    public void bookingApproveValidator(Long userId, Long bookingId) {
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
//            List<Booking> allByItemId = bookingRepository.findAllByItemIdExcludingRejectedAndPast(booking.getItem().getId());
//            if (allByItemId.size() == 1) {
//                booking.setStatus(BookingStatus.CURRENT);
//            }
//            if (allByItemId.size() > 1) {
//                Booking currentBooking = allByItemId.stream()
//                        .filter(booking1 -> booking1.getStatus().equals(BookingStatus.CURRENT))
//                        .findFirst().get();
//                if (currentBooking.getBooker().getId() == booking.getBooker().getId()) {
//                    currentBooking.setStatus(BookingStatus.PAST);
//                    booking.setStatus(BookingStatus.CURRENT);
//                } else booking.setStatus(BookingStatus.FUTURE);
//            }
//            booking.getItem().getBookings().addAll(allByItemId);