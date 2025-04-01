package com.att.tdp.popcorn_palace.Services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Repositories.MovieRepository;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTests {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    public void MovieService_CreateMovie_SuccsesTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        Movie movieMock = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        when(movieRepository.findByTitle(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(movieMock);

        Movie newMovie = assertDoesNotThrow(() -> movieService.createMovie(movie));

        Assertions.assertThat(newMovie).isNotNull();
        Assertions.assertThat(newMovie.getTitle()).isEqualTo(movie.getTitle());
        Assertions.assertThat(newMovie.getDuration()).isEqualTo(movie.getDuration());
        Assertions.assertThat(newMovie.getRating()).isEqualTo(movie.getRating());
        Assertions.assertThat(newMovie.getReleaseYear()).isEqualTo(movie.getReleaseYear());
        Assertions.assertThat(newMovie.getGenre()).isEqualTo(movie.getGenre());
    }

    @Test
    public void MovieService_CreateMovie_FailByDontHaveTitleTest()
    {
        Movie movie = Movie.builder()
            //.title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        Assertions.assertThatThrownBy(() -> movieService.createMovie(movie))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie title and genre are required!");
    }

    @Test
    public void MovieService_CreateMovie_FailByDontHaveGenreTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            //.genre("Action")
            .build();

        Assertions.assertThatThrownBy(() -> movieService.createMovie(movie))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie title and genre are required!");
    }

    @Test
    public void MovieService_CreateMovie_FailByDurationTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            //.duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

            Movie movie2 = Movie.builder()
            .title("Movie 1")
            .duration(0)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

            Movie movie3 = Movie.builder()
            .title("Movie 1")
            .duration(-1)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();


        Assertions.assertThatThrownBy(() -> movieService.createMovie(movie))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie duration must be greater than 0!");

        Assertions.assertThatThrownBy(() -> movieService.createMovie(movie2))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie duration must be greater than 0!");

        Assertions.assertThatThrownBy(() -> movieService.createMovie(movie3))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie duration must be greater than 0!");
    }

    @Test
    public void MovieService_CreateMovie_FailByAlreadyExistsTest()
    {
        Movie movie = Movie.builder()
        .title("Movie 1")
        .duration(90)
        .rating(3.8f)
        .releaseYear(2020)
        .genre("Action")
        .build();

        Movie movieMock = Movie.builder().build();

        when(movieRepository.findByTitle(Mockito.any(String.class))).thenReturn(Optional.of(movieMock));

        Assertions.assertThatThrownBy(() -> movieService.createMovie(movie))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie with this title already exists!");
    }

    @Test
    public void MovieService_UpdateMovie_SuccessTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

            Movie updatedMovie = Movie.builder()
            .title("Updated Movie")
            .duration(120)
            .rating(4.5f)
            .releaseYear(2021)
            .genre("Drama")
            .build();

        when(movieRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(movie));
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(updatedMovie);

        Movie updatedMovieMock = assertDoesNotThrow(() -> movieService.updateMovie(1L, updatedMovie));

        Assertions.assertThat(updatedMovieMock).isNotNull();
        Assertions.assertThat(updatedMovieMock.getTitle()).isEqualTo(updatedMovie.getTitle());
        Assertions.assertThat(updatedMovieMock.getDuration()).isEqualTo(updatedMovie.getDuration());
        Assertions.assertThat(updatedMovieMock.getRating()).isEqualTo(updatedMovie.getRating());
        Assertions.assertThat(updatedMovieMock.getReleaseYear()).isEqualTo(updatedMovie.getReleaseYear());
        Assertions.assertThat(updatedMovieMock.getGenre()).isEqualTo(updatedMovie.getGenre());
    }

    @Test
    public void MovieService_UpdateMovie_FailedByNotFoundIdTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

            when(movieRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> movieService.updateMovie(0L, movie))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie not found!");
    }

    @Test
    public void MovieService_UpdateMovie_SuccessByTitleTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

            Movie updatedMovie = Movie.builder()
            .title("Updated Movie")
            .duration(120)
            .rating(4.5f)
            .releaseYear(2021)
            .genre("Drama")
            .build();

        when(movieRepository.findByTitle(Mockito.any(String.class))).thenReturn(Optional.of(movie));
        when(movieRepository.save(Mockito.any(Movie.class))).thenReturn(updatedMovie);

        Movie updatedMovieMock = assertDoesNotThrow(() -> movieService.updateMovieByTitle("Movie 1", updatedMovie));

        Assertions.assertThat(updatedMovieMock).isNotNull();
        Assertions.assertThat(updatedMovieMock.getTitle()).isEqualTo(updatedMovie.getTitle());
        Assertions.assertThat(updatedMovieMock.getDuration()).isEqualTo(updatedMovie.getDuration());
        Assertions.assertThat(updatedMovieMock.getRating()).isEqualTo(updatedMovie.getRating());
        Assertions.assertThat(updatedMovieMock.getReleaseYear()).isEqualTo(updatedMovie.getReleaseYear());
        Assertions.assertThat(updatedMovieMock.getGenre()).isEqualTo(updatedMovie.getGenre());
    }

    @Test
    public void MovieService_UpdateMovie_FailedByNotFoundTitleTest()
    {
        Movie movie = Movie.builder()
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

            when(movieRepository.findByTitle(Mockito.any(String.class))).thenReturn(Optional.empty());

            Assertions.assertThatThrownBy(() -> movieService.updateMovieByTitle("Movie 1", movie))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie not found!");
    }

    @Test
    public void MovieService_DeleteMovie_SuccessTest()
    {
        Movie movie = Movie.builder()
            .id(1L)
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        when(movieRepository.existsById(Mockito.any(Long.class))).thenReturn(true);

        assertDoesNotThrow(() -> movieService.deleteMovie(movie.getId()));
    }

    @Test
    public void MovieService_DeleteMovie_FailedByNotExistIdTest()
    {
        Movie movie = Movie.builder()
            .id(1L)
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        when(movieRepository.existsById(Mockito.any(Long.class))).thenReturn(false);

        Assertions.assertThatThrownBy(() -> movieService.deleteMovie(movie.getId()))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie not found!");
    }

    @Test
    public void MovieService_DeleteMovie_SuccessByTitleTest()
    {
        Movie movie = Movie.builder()
            .id(1L)
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        when(movieRepository.findByTitle(Mockito.any(String.class))).thenReturn(Optional.of(movie));

        assertDoesNotThrow(() -> movieService.deleteMovieByTitle(movie.getTitle()));
    }

    @Test
    public void MovieService_DeleteMovie_FailedByNotExistTitleTest()
    {
        Movie movie = Movie.builder()
            .id(1L)
            .title("Movie 1")
            .duration(90)
            .rating(3.8f)
            .releaseYear(2020)
            .genre("Action")
            .build();

        when(movieRepository.findByTitle(Mockito.any(String.class))).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> movieService.deleteMovieByTitle(movie.getTitle()))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Movie not found!");
    }
}
