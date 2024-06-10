package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@Slf4j
public class BookingServiceImpl implements BookingService {


    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingResponse createBooking(Long bookerId, BookingDto bookingDto) {
        log.info("Создание нового бронирования пользователем с id {}", bookerId);
        bookingValidator(bookingDto);
        boolean renterExist = userRepository.existsById(bookerId);
        if (!renterExist) {
            throw new NotExistException("Не найден пользователь");
        }
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotExistException("Не найден предмет"));
        if (item.getUser().getId().equals(bookerId)) {
            throw new BookingException("Владелец не может бронировать свою вещь");
        }
        User owner = item.getUser();
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new NotExistException("Не найден пользователь"));
        Booking booking = BookingMapper.mapToBooking(item, booker, owner, bookingDto);
        bookingRepository.save(booking);
        item.getBookings().add(booking);
        log.info("Бронирование успешно создано с id {}", booking.getId());
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse approveBooking(Long bookingId, String approved, Long ownerId) {
        log.info("Подтверждение бронирования с id {} владельцем с id {}", bookingId, ownerId);
        bookingApproveValidator(ownerId, bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotExistException("Не существует бронирование с id %d", bookingId));
        booking.setStatus("true".equals(approved) ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        bookingRepository.save(booking);
        log.info("Бронирование с id {} успешно {} владельцем с id {}", bookingId, approved.equals("true") ? "подтверждено" : "отклонено", ownerId);
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public BookingResponse findById(Long bookingId, Long userId) {
        log.info("Поиск бронирования с id {} для пользователя с id {}", bookingId, userId);
        verifyUserExists(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotExistException("Не существует бронирование с id %d", bookingId));
        boolean userIsBookerOrOwner = booking.getBooker().getId().equals(userId) || booking.getOwner().getId().equals(userId);
        if (!userIsBookerOrOwner) {
            throw new NotExistException("У пользователя с id %d нет бронирований", userId);
        }
        log.info("Бронирование с id {} найдено для пользователя с id {}", bookingId, userId);
        return BookingMapper.mapToBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> getBookingsByBooker(Long userId, String state, int from, int size) {
        log.info("Запрос всех бронирований для пользователя с id {} с состоянием {}", userId, state);
        verifyUserExists(userId);
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, pageable);
        return filterBookingsByState(bookings, state);
    }

    @Override
    public List<BookingResponse> getBookingsByOwner(Long userId, String state, int from, int size) {
        log.info("Запрос всех бронирований для владельца с id {} с состоянием {}", userId, state);
        verifyUserExists(userId);
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, pageable);
        return filterBookingsByState(bookings, state);
    }

    @Override
    public List<BookingResponse> allBookingsByBooker(Long bookerId, int from, int size) {
        log.info("Запрос всех бронирований для пользователя с id {}", bookerId);
        verifyUserExists(bookerId);
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId, pageable);
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponse> allBookingsByOwner(Long userId, int from, int size) {
        log.info("Запрос всех бронирований для владельца с id {}", userId);
        verifyUserExists(userId);
        Pageable pageable = PageRequest.of(from/size, size);
        Page<Booking> bookings = bookingRepository.findAllByOwnerIdOrderByStartDesc(userId, pageable);
        return bookings.stream()
                .map(BookingMapper::mapToBookingResponse)
                .sorted(Comparator.comparing(BookingResponse::getStart).reversed())
                .collect(Collectors.toList());
    }

    private List<BookingResponse> filterBookingsByState(Page<Booking> bookings, String state) {
        log.info("Фильтрация бронирований по состоянию {}", state);
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
        log.info("Валидация бронирования для предмета с id {}", bookingDto.getItemId());
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
        log.info("Проверка существования пользователя с id {}", userId);
        boolean userExists = userRepository.existsById(userId);
        if (!userExists) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
    }

    private void bookingApproveValidator(Long userId, Long bookingId) {
        log.info("Валидация подтверждения бронирования с id {} владельцем с id {}", bookingId, userId);
        boolean userExist = userRepository.existsById(userId);
        boolean bookingExist = bookingRepository.existsById(bookingId);
        if (!userExist) {
            throw new NotExistException("Не существует пользователь с id %d", userId);
        }
        if (!bookingExist) {
            throw new NotExistException("Не существует бронирование с id %d", bookingId);
        }
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotExistException("Не существует бронирование с id %d", bookingId));
        if (booking.getBooker().getId().equals(userId)) {
            throw new BookingException("Вам не возможно изменить бронирование");
        }
        if (!booking.getStatus().equals(BookingStatus.WAITING)) {
            throw new DataNotFoundException("Бронирование уже одобрено");
        }
    }
}
