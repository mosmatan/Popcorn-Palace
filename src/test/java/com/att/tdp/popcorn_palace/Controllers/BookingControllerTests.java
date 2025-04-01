package com.att.tdp.popcorn_palace.Controllers;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Services.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = BookingController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class BookingControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private BookingService bookingService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void BookingController_CreateBooking_SuccessTest() throws Exception {
        Booking booking = Booking.builder()
                .bookingId("12345")
                .showtime(Showtime.builder().id(1L).build())
                .seatNumber(5)
                .userId("user123")
                .build();
    
        when(bookingService.createBooking(ArgumentMatchers.any())).thenReturn(booking);
    
        ResultActions result = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)));
    
        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.bookingId").value("12345"));
    }

    @Test
    public void BookingController_CreateBooking_FailureTest() throws Exception {
        Booking booking = Booking.builder()
                .bookingId("12345")
                .showtime(Showtime.builder().id(1L).build())
                .seatNumber(5)
                .userId("user123")
                .build();
    
        when(bookingService.createBooking(ArgumentMatchers.any())).thenThrow(new RuntimeException("Booking failed"));
    
        ResultActions result = mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)));
    
        result.andExpect(status().isBadRequest())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.error").value("Booking failed"));
    }
}
