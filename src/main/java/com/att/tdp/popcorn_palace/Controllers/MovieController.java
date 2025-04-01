package com.att.tdp.popcorn_palace.Controllers;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Models.ErrorResponseObject;
import com.att.tdp.popcorn_palace.Services.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // GET /movies/all
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> allMovies = movieService.getAllMovies();
        return ResponseEntity.ok(allMovies); 
    }

    // POST /movies
    @PostMapping
    public ResponseEntity<Object> addMovie(@RequestBody Movie movie) {
        try {

            return ResponseEntity.ok(movieService.createMovie(movie));
        } 
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
    }

    // POST /movies/update/{movieTitle}
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<Object> updateMovieByTitle(
            @PathVariable String movieTitle,
            @RequestBody Movie updatedMovie
    ) {
        try
        {
            movieService.updateMovieByTitle(movieTitle, updatedMovie);
            return ResponseEntity.ok(null);
        }
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

    }

    // DELETE /movies/{movieTitle}
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<Object> deleteMovie(@PathVariable String movieTitle) {
        try
        {
            movieService.deleteMovieByTitle(movieTitle);
            return ResponseEntity.ok(null);
        }
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
        
    }
}

