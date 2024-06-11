//package ru.practicum.shareit.booking;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import ru.practicum.shareit.booking.model.BookingDto;
//import ru.practicum.shareit.booking.model.BookingResponse;
//import ru.practicum.shareit.booking.model.BookingStatus;
//import ru.practicum.shareit.exception.UnsupportedStatusException;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(controllers = BookingController.class)
//public class BookingControllerTestWithExceptions {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private BookingService bookingService;
//
//    private BookingDto bookingDto;
//    private BookingResponse bookingResponse;
//
//    @BeforeEach
//    public void setup() {
//        bookingDto = BookingDto.builder()
//                .id(1L)
//                .start(LocalDateTime.now().plusDays(1))
//                .end(LocalDateTime.now().plusDays(2))
//                .itemId(1L)
//                .build();
//
//        bookingResponse = BookingResponse.builder()
//                .id(1L)
//                .start(LocalDateTime.now().plusDays(1))
//                .end(LocalDateTime.now().plusDays(2))
//                .status(BookingStatus.WAITING)
//                .build();
//    }
//
//    @Test
//    public void testCreateBooking() throws Exception {
//        given(bookingService.createBooking(anyLong(), any(BookingDto.class))).willReturn(bookingResponse);
//
//        mockMvc.perform(post("/bookings")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header("X-Sharer-User-Id", 1L)
//                        .content(objectMapper.writeValueAsString(bookingDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingResponse.getId().intValue())))
//                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())));
//    }
//
//    @Test
//    public void testApproveBooking() throws Exception {
//        given(bookingService.approveBooking(anyLong(), eq("true"), anyLong())).willReturn(bookingResponse);
//
//        mockMvc.perform(patch("/bookings/1")
//                        .param("approved", "true")
//                        .header("X-Sharer-User-Id", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingResponse.getId().intValue())))
//                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())));
//    }
//
//    @Test
//    public void testFindById() throws Exception {
//        given(bookingService.findById(anyLong(), anyLong())).willReturn(bookingResponse);
//
//        mockMvc.perform(get("/bookings/1")
//                        .header("X-Sharer-User-Id", 1L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingResponse.getId().intValue())))
//                .andExpect(jsonPath("$.status", is(bookingResponse.getStatus().toString())));
//    }
//
//    @Test
//    public void testAllBookingsByBooker() throws Exception {
//        List<BookingResponse> bookings = Arrays.asList(bookingResponse, bookingResponse);
//        given(bookingService.allBookingsByBooker(anyLong(), anyInt(), anyInt())).willReturn(bookings);
//
//        mockMvc.perform(get("/bookings")
//                        .header("X-Sharer-User-Id", 1L)
//                        .param("from", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(bookingResponse.getId().intValue())))
//                .andExpect(jsonPath("$[1].id", is(bookingResponse.getId().intValue())));
//    }
//
//    @Test
//    public void testAllBookingsByOwner() throws Exception {
//        List<BookingResponse> bookings = Arrays.asList(bookingResponse, bookingResponse);
//        given(bookingService.allBookingsByOwner(anyLong(), anyInt(), anyInt())).willReturn(bookings);
//
//        mockMvc.perform(get("/bookings/owner")
//                        .header("X-Sharer-User-Id", 1L)
//                        .param("from", "0")
//                        .param("size", "10"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id", is(bookingResponse.getId().intValue())))
//                .andExpect(jsonPath("$[1].id", is(bookingResponse.getId().intValue())));
//    }
//
//    @Test
//    public void testApproveBookingWithInvalidParam() throws Exception {
//        mockMvc.perform(patch("/bookings/1")
//                        .param("approved", "invalid")
//                        .header("X-Sharer-User-Id", 1L))
//                .andExpect(status().isConflict())
//                .andExpect(content().string("Неправильно передан параметр approved"));
//    }
//
//    @Test
//    public void testGetBookingsByBookerWithUnsupportedState() throws Exception {
//        given(bookingService.getBookingsByBooker(anyLong(), anyString(), anyInt(), anyInt()))
//                .willThrow(new UnsupportedStatusException("UNSUPPORTED_STATUS"));
//
//        mockMvc.perform(get("/bookings")
//                        .header("X-Sharer-User-Id", 1L)
//                        .param("state", "unsupported"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.error", is("Unknown state: UNSUPPORTED_STATUS")));
//    }
//}
