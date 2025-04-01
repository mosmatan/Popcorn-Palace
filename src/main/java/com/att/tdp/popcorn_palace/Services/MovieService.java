package com.att.tdp.popcorn_palace.Services;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie createMovie(Movie movie) {

        // Ensure the movie has a title and genre
        if (movie.getTitle() == null || movie.getGenre() == null) {
            throw new RuntimeException("Movie title and genre are required!");
        }
        // Ensure the movie has a valid duration and rating
        if (movie.getDuration() <= 0) {
            throw new RuntimeException("Movie duration must be greater than 0!");
        }

        // Check if a movie with the same title already exists
        if (movieRepository.findByTitle(movie.getTitle()).isPresent()) {
            throw new RuntimeException("Movie with this title already exists!");
        }

        try
        {
            return movieRepository.save(movie);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create movie");
        }
    }

    public Movie updateMovie(Long movieId, Movie updatedMovie) {
        //fetch existing
        Movie existing = movieRepository.findById(movieId)
            .orElseThrow(() -> new RuntimeException("Movie not found!"));

        try
        {
            return updateMovieData(existing, updatedMovie);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update movie");
        }
        
    }

    public void deleteMovie(Long movieId) {
        // Check if the movie exists
        if (!movieRepository.existsById(movieId)) {
            throw new RuntimeException("Movie not found!");
        }
        
        try
        {
            movieRepository.deleteById(movieId);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to delete movie");
        }
        
    }

    public List<Movie> getAllMovies() {
        try
        {
            return movieRepository.findAll();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to fetch movies");
        }
    }

    public Movie updateMovieByTitle(String movieTitle, Movie updatedMovie) {

        //fetch existing by title
        Movie existing = movieRepository.findByTitle(movieTitle)
            .orElseThrow(() -> new RuntimeException("Movie not found!"));

        try
        {
            return updateMovieData(existing, updatedMovie);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update movie");
        }
    }

    public void deleteMovieByTitle(String movieTitle) {
        Movie existing = movieRepository.findByTitle(movieTitle)
        .orElseThrow(() -> new RuntimeException("Movie not found!"));

        try
        {
            movieRepository.delete(existing);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to delete movie");
        }
        
    }

    private Movie updateMovieData(Movie existing, Movie updatedMovie) {

        if (updatedMovie.getTitle() != null) {
            existing.setTitle(updatedMovie.getTitle());
        }
        if (updatedMovie.getGenre() != null) {
            existing.setGenre(updatedMovie.getGenre());
        }
        if (updatedMovie.getDuration() > 0) {
            existing.setDuration(updatedMovie.getDuration());
        }
        if (updatedMovie.getRating() >= 0) {
            existing.setRating(updatedMovie.getRating());
        }
        if (updatedMovie.getReleaseYear() > 0) {
            existing.setReleaseYear(updatedMovie.getReleaseYear());
        }

        try
        {
            return movieRepository.save(existing);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to update movie data");
        }
        
    }

}
