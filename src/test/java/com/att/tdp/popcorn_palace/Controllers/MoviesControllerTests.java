package com.att.tdp.popcorn_palace.Controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.springframework.http.MediaType;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MoviesControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;
    

    @Test
    public void MovieController_CreateMovie_SuccessTest() throws Exception {

        Movie movie = Movie.builder()
                .title("Inception")
                .releaseYear(2010)
                .genre("Sci-Fi")
                .rating(8.8)
                .build();


        when(movieService.createMovie(ArgumentMatchers.any())).thenReturn(movie);

        ResultActions result = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)));


        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value(movie.getTitle()))
                .andExpect(jsonPath("$.releaseYear").value(movie.getReleaseYear()))
                .andExpect(jsonPath("$.genre").value(movie.getGenre()))
                .andExpect(jsonPath("$.rating").value(movie.getRating()));

                
    }

    @Test
    public void MovieController_CreateMovie_NotExistsMovie_FailureTest() throws Exception {

        Movie movie = Movie.builder()
                .title("Inception")
                .releaseYear(2010)
                .genre("Sci-Fi")
                .rating(8.8)
                .build();


        when(movieService.createMovie(ArgumentMatchers.any())).thenThrow(new RuntimeException("Movie already exists"));

        ResultActions result = mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)));


        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Movie already exists"));

    }

    @Test
    public void MovieController_GetAllMovies_SuccessTest() throws Exception{

        Movie movie1 = Movie.builder()
                .title("Inception")
                .releaseYear(2010)
                .genre("Sci-Fi")
                .rating(8.8)
                .build();

        Movie movie2 = Movie.builder()
                .title("The Dark Knight")
                .releaseYear(2008)
                .genre("Action")
                .rating(9.0)
                .build();


        when(movieService.getAllMovies()).thenReturn(List.of(movie1, movie2));

        ResultActions result = mockMvc.perform(get("/movies/all")
                        .contentType(MediaType.APPLICATION_JSON));


        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value(movie1.getTitle()))
                .andExpect(jsonPath("$[0].releaseYear").value(movie1.getReleaseYear()))
                .andExpect(jsonPath("$[0].genre").value(movie1.getGenre()))
                .andExpect(jsonPath("$[0].rating").value(movie1.getRating()))
                .andExpect(jsonPath("$[1].title").value(movie2.getTitle()))
                .andExpect(jsonPath("$[1].releaseYear").value(movie2.getReleaseYear()))
                .andExpect(jsonPath("$[1].genre").value(movie2.getGenre()))
                .andExpect(jsonPath("$[1].rating").value(movie2.getRating()));
    }

    @Test
    public void MovieController_UpdateMovie_SuccessTest() throws Exception{

        Movie movie = Movie.builder()
                .title("Inception")
                .releaseYear(2010)
                .genre("Sci-Fi")
                .rating(8.8)
                .build();


        when(movieService.updateMovieByTitle(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(movie);

        ResultActions result = mockMvc.perform(post("/movies/update/{movieTitle}", movie.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)));


        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void MovieController_UpdateMovie_NotExistsMovie_FailureTest() throws Exception{

        Movie movie = Movie.builder()
                .title("Inception")
                .releaseYear(2010)
                .genre("Sci-Fi")
                .rating(8.8)
                .build();


        when(movieService.updateMovieByTitle(ArgumentMatchers.any(), ArgumentMatchers.any())).thenThrow(new RuntimeException("Movie not found"));

        ResultActions result = mockMvc.perform(post("/movies/update/{movieTitle}", movie.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movie)));


        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Movie not found"));
    }

    @Test
    public void MovieController_DeleteMovie_SuccessTest() throws Exception{

        String movieTitle = "Inception";
            
        doNothing().when(movieService).deleteMovieByTitle(ArgumentMatchers.any());

        ResultActions result = mockMvc.perform(delete("/movies/{movieTitle}", movieTitle));

        result.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void MovieController_DeleteMovie_NotExistsMovie_FailureTest() throws Exception{

        String movieTitle = "Inception";
            
        doThrow(new RuntimeException("Movie not found")).when(movieService).deleteMovieByTitle(ArgumentMatchers.any());

        ResultActions result = mockMvc.perform(delete("/movies/{movieTitle}", movieTitle));

        result.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Movie not found"));
    }
}
