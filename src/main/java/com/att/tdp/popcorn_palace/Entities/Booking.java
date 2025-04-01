package com.att.tdp.popcorn_palace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

/**
 * Bookings table. Each record links to a showtime, a specific seat, and a user identifier.
 */
@Entity
@Table(
    name = "bookings",
    uniqueConstraints = {
        // This ensures a seat is not double-booked for the same showtime
        //@UniqueConstraint(columnNames = {"showtime_id", "seatNumber"})
        @UniqueConstraint(columnNames = {"bookingId"})
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    // Use UUID-based PK
    private String bookingId = UUID.randomUUID().toString();

    // Link to showtime
    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    private int seatNumber;
    private String userId;

}

