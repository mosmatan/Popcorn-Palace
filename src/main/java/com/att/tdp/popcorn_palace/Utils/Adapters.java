package com.att.tdp.popcorn_palace.Utils;

import com.att.tdp.popcorn_palace.Entities.*;
import com.att.tdp.popcorn_palace.Models.Requests.*;
import com.att.tdp.popcorn_palace.Models.Responses.*;


public class Adapters {

    // Converts a Booking entity to a BookingResponse object
    public static BookingResponse ToBookingResponse(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        return response;
    }

    // Converts a BookingRequest object to a Booking entity
    public static Booking ToBooking(BookingRequest bookingRequest) {
        return Booking.builder()
                .showtime(Showtime.builder().id(bookingRequest.getShowtimeId()).build())
                .seatNumber(bookingRequest.getSeatNumber())
                .userId(bookingRequest.getUserId())
                .build();
    }

    // Converts a Showtime entity to a ShowtimeAddResponse object
    public static ShowtimeAddResponse ToShowtimeAddResponse(Showtime showtime) {
        ShowtimeAddResponse response = new ShowtimeAddResponse();

        response.setId(showtime.getId());
        response.setMovieId(showtime.getMovie().getId());
        response.setTheater(showtime.getTheater());
        response.setStartTime(showtime.getStartTime());
        response.setEndTime(showtime.getEndTime());
        response.setPrice(showtime.getPrice());

        return response;
    }

    // Converts a ShowtimeRequest object to a Showtime entity
    // Not updating the movie field here
    public static Showtime ToShowtime(ShowtimeRequest showtimeRequest) {
        return Showtime.builder()
                .theater(showtimeRequest.getTheater())
                .startTime(showtimeRequest.getStartTime())
                .endTime(showtimeRequest.getEndTime())
                .price(showtimeRequest.getPrice())
                .build();
    }
}
