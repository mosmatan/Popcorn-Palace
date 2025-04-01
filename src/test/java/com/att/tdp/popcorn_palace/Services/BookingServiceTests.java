package com.att.tdp.popcorn_palace.Services;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Models.Requests.BookingRequest;
import com.att.tdp.popcorn_palace.Repositories.BookingRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTests {

    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private BookingService bookingService;
    

    @Test
    public void BookingService_CreateBooking_ValidRequest_SuccessTest() {
        BookingRequest bookingRequest = BookingRequest.builder()
                .showtimeId(1L)
                .seatNumber(0)
                .userId("testUser")
                .build();
        
        Showtime showtime = Showtime.builder()
                .id(1L)
                .build();

        Booking booking = Booking.builder()
                .showtime(showtime)
                .seatNumber(0)
                .userId("testUser")
                .build();

        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(showtime));
        when(bookingRepository.existsByShowtimeAndSeatNumber(Mockito.any(Showtime.class), Mockito.anyInt())).thenReturn(false);
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);

        Booking result = assertDoesNotThrow(() -> bookingService.createBooking(bookingRequest));
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(booking.getShowtime()).isEqualTo(result.getShowtime());
        Assertions.assertThat(booking.getSeatNumber()).isEqualTo(result.getSeatNumber());
        Assertions.assertThat(booking.getUserId()).isEqualTo(result.getUserId());
    }

    @Test
    public void BookingService_CreateBooking_ShowtimeNotFoundTest() {
        BookingRequest bookingRequest = BookingRequest.builder()
                .showtimeId(1L)
                .seatNumber(0)
                .userId("testUser")
                .build();

        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Showtime not found");
    }

    @Test
    public void BookingService_CreateBooking_SeatTaken_FailureTest()
    {
        BookingRequest bookingRequest = BookingRequest.builder()
                .showtimeId(1L)
                .seatNumber(0)
                .userId("testUser")
                .build();

        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(new Showtime()));
        when(bookingRepository.existsByShowtimeAndSeatNumber(Mockito.any(Showtime.class), Mockito.anyInt())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> bookingService.createBooking(bookingRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Seat already booked for this showtime!");
    }

}
