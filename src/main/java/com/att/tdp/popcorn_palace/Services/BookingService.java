package com.att.tdp.popcorn_palace.Services;
import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Models.Requests.BookingRequest;
import com.att.tdp.popcorn_palace.Repositories.BookingRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;

import java.util.UUID;

import org.springframework.stereotype.Service;
import com.att.tdp.popcorn_palace.Utils.Adapters;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;

    public BookingService(BookingRepository bookingRepository,
                          ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public Booking createBooking(BookingRequest booking) {
        // Ensure the showtime exists
        Showtime st = showtimeRepository.findById(booking.getShowtimeId())
            .orElseThrow(() -> new RuntimeException("Showtime not found!"));

        // Check if the seat is already booked for this showtime
        if (bookingRepository.existsByShowtimeAndSeatNumber(st, booking.getSeatNumber())) {
            throw new RuntimeException("Seat already booked for this showtime!");
        }

        Booking newBooking = Adapters.ToBooking(booking);
        newBooking.setBookingId(UUID.randomUUID().toString());
        newBooking.setShowtime(st);

        try
        {
            return bookingRepository.save(newBooking);

        } catch (Exception e) {
            throw new RuntimeException("Failed to create booking: " + e.getMessage());
        }
    }
}
