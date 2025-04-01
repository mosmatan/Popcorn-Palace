package com.att.tdp.popcorn_palace.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * Showtimes table. Each record references a movie, plus start/end times, price, and theater info.
 */
@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Showtime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double price;
    private String theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Relationship to Movie
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;
}

