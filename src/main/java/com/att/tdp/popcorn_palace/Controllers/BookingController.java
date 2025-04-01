package com.att.tdp.popcorn_palace.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.att.tdp.popcorn_palace.Services.BookingService;
import com.att.tdp.popcorn_palace.Entities.Booking;
import com.att.tdp.popcorn_palace.Models.ErrorResponseObject;
import com.att.tdp.popcorn_palace.Models.Requests.BookingRequest;
import com.att.tdp.popcorn_palace.Models.Responses.BookingResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST /bookings
    @PostMapping
    public ResponseEntity<Object> addBooking(@RequestBody BookingRequest booking) {
        try {

            Booking newBooking = bookingService.createBooking(booking);

            BookingResponse response = new BookingResponse();
            response.setBookingId(newBooking.getBookingId());

            return ResponseEntity.ok(response);
        } 
        catch (RuntimeException e) {
            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
