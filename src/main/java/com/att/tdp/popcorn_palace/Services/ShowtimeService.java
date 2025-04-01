package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Models.Requests.ShowtimeRequest;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Utils.Adapters;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final MovieRepository movieRepository;

    public ShowtimeService(ShowtimeRepository showtimeRepository , MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public Showtime createShowtime(ShowtimeRequest showtimeRequest) {

        Showtime showtime = Adapters.ToShowtime(showtimeRequest);

        // Check overlapping showtime in the same theater
        validateNoOverlap(showtime);

        // Check if the movie exists
        Movie movie = movieRepository.findById(showtimeRequest.getMovieId())
            .orElseThrow(() -> new RuntimeException("MovieId not found!"));

        showtime.setMovie(movie);

        try
        {
            return showtimeRepository.save(showtime);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create showtime");
        }
    }

    public Showtime updateShowtime(Long showtimeId, ShowtimeRequest updatedRequest) {
        // Fetch existing
        Showtime existing = showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> new RuntimeException("Showtime not found!"));

        Showtime updated = Adapters.ToShowtime(updatedRequest);

        // Check if the movie exists
        // If movieId is null, do not update the movie field
        if (updatedRequest.getMovieId() != null) {
            Movie movie = movieRepository.findById(updatedRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("MovieId not found!"));
            updated.setMovie(movie);
        }

        // Update fields
        updateShowtimeData(existing, updated);

        // Overlap check
        validateNoOverlap(existing);

        try
        {
            return showtimeRepository.save(existing);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update showtime");
        }
    }

    public Showtime getShowtimeById(Long showtimeId) {
        return showtimeRepository.findById(showtimeId)
            .orElseThrow(() -> new RuntimeException("Showtime not found!"));
    }

    public void deleteShowtime(Long showtimeId) {
        // Check if the showtime exists
        if (!showtimeRepository.existsById(showtimeId)) {
            throw new RuntimeException("Showtime not found!");
        }

        try
        {
            showtimeRepository.deleteById(showtimeId);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to delete showtime");
        }
    }

    // Overlap logic
    private void validateNoOverlap(Showtime newShowtime) {
        //Get all showtimes in the same theater
        List<Showtime> allInSameTheater = 
            showtimeRepository.findAll()
                                .stream()
                                .filter(st -> st.getTheater().equals(newShowtime.getTheater()))
                                .toList();

        for (Showtime st : allInSameTheater) {
            // skip checking against itself if updating
            if (st.getId() != null && st.getId() == newShowtime.getId()) {
                continue;
            }
            
            if (checkOverlap(st, newShowtime)) {
                throw new RuntimeException("Cannot create/update showtime: Overlaps with existing showtime!");
            }
        }
    }

    private boolean checkOverlap(Showtime first, Showtime second) {
        LocalDateTime firstStart = first.getStartTime();
        LocalDateTime firstEnd = first.getEndTime();
        LocalDateTime secondStart = second.getStartTime();
        LocalDateTime secondEnd = second.getEndTime();

        return (firstStart.isBefore(secondEnd) && firstEnd.isAfter(secondStart)) || // overlaps
               (firstStart.isEqual(secondStart) || firstEnd.isEqual(secondEnd)) || // touching
               (firstStart.isEqual(secondEnd) || firstEnd.isEqual(secondStart)) || // touching
               (firstStart.isAfter(secondStart) && firstEnd.isBefore(secondEnd)) || // inside
               (firstStart.isBefore(secondStart) && firstEnd.isAfter(secondEnd)); // contains
    }

    private void updateShowtimeData(Showtime existing, Showtime updated) {
        if (updated.getTheater() != null) {
            existing.setTheater(updated.getTheater());
        }
        if (updated.getPrice() >= 0f) {
            existing.setPrice(updated.getPrice());
        }
        if(updated.getMovie() != null) {
            existing.setMovie(updated.getMovie());
        }

        updateShowtimesTimes(existing, updated);
    }

    private void updateShowtimesTimes(Showtime existing, Showtime updated) {
        if (updated.getStartTime() != null) {

            if (updated.getEndTime() != null && updated.getStartTime().isAfter(updated.getEndTime())) {
                throw new RuntimeException("Start time cannot be after end time!");
            }
            else if (updated.getEndTime() == null && updated.getStartTime().isAfter(existing.getEndTime())) {
                throw new RuntimeException("Start time cannot be after end time!");
            }
            existing.setStartTime(updated.getStartTime());
        }

        if (updated.getEndTime() != null) {
            if (updated.getStartTime() != null && updated.getEndTime().isBefore(updated.getStartTime())) {
                throw new RuntimeException("End time cannot be before start time!");
            }
            else if (updated.getStartTime() == null && updated.getEndTime().isBefore(existing.getStartTime())) {
                throw new RuntimeException("End time cannot be before start time!");
            }
            existing.setEndTime(updated.getEndTime());
        }
    }

}