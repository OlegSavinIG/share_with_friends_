package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingResponse;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UnsupportedStatusException;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingDto bookingDto;
    private BookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingDto = BookingDto.builder()
                .id(1L)
                .itemId(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .build();

        bookingResponse = BookingResponse.builder()
                .id(1L)
                .booker(UserDto.builder().id(1L).name("Booker").build())
                .item(ItemDto.builder().id(1L).name("Item").build())
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(2))
                .status(BookingStatus.WAITING)
                .build();
    }

    @Test
    void testCreateBooking() throws Exception {
        given(bookingService.createBooking(anyLong(), any(BookingDto.class))).willReturn(bookingResponse);

        mockMvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.booker.id").value(bookingResponse.getBooker().getId()))
                .andExpect(jsonPath("$.item.id").value(bookingResponse.getItem().getId()))
                .andExpect(jsonPath("$.status").value(bookingResponse.getStatus().toString()));
    }

    @Test
    void testApproveBooking() throws Exception {
        given(bookingService.approveBooking(anyLong(), anyString(), anyLong())).willReturn(bookingResponse);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.status").value(bookingResponse.getStatus().toString()));
    }


    @Test
    void testFindById() throws Exception {
        given(bookingService.findById(anyLong(), anyLong())).willReturn(bookingResponse);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$.booker.id").value(bookingResponse.getBooker().getId()))
                .andExpect(jsonPath("$.item.id").value(bookingResponse.getItem().getId()))
                .andExpect(jsonPath("$.status").value(bookingResponse.getStatus().toString()));
    }

    @Test
    void testAllBookingsByBooker() throws Exception {
        List<BookingResponse> bookings = Collections.singletonList(bookingResponse);
        given(bookingService.allBookingsByBooker(anyLong(), anyInt(), anyInt())).willReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingResponse.getBooker().getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingResponse.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponse.getStatus().toString()));
    }

    @Test
    void testAllBookingsByOwner() throws Exception {
        List<BookingResponse> bookings = Collections.singletonList(bookingResponse);
        given(bookingService.allBookingsByOwner(anyLong(), anyInt(), anyInt())).willReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "all")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingResponse.getId()))
                .andExpect(jsonPath("$[0].booker.id").value(bookingResponse.getBooker().getId()))
                .andExpect(jsonPath("$[0].item.id").value(bookingResponse.getItem().getId()))
                .andExpect(jsonPath("$[0].status").value(bookingResponse.getStatus().toString()));
    }

    @Test
    void testAllBookingsByBookerInvalidState() throws Exception {
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "invalid")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(result -> result.getResolvedException().getClass().equals(UnsupportedStatusException.class));
    }
}
