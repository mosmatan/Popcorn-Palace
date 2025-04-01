package com.att.tdp.popcorn_palace.Controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Models.Requests.ShowtimeRequest;
import com.att.tdp.popcorn_palace.Services.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;

@WebMvcTest(controllers = ShowtimeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ShowtimesControllerTests {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    private ShowtimeService showtimeService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void ShowtimeController_GetShowtimeById_SuccessTest() throws Exception {

        Long showtimeId = 1L;
        Showtime showtime = Showtime.builder()
                .id(showtimeId)
                .theater("Theater 1")
                .startTime(LocalDateTime.of(2023, 10, 1, 12, 0))
                .endTime(LocalDateTime.of(2023, 10, 1, 14, 0))
                .price(12.50)
                .build();

        when(showtimeService.getShowtimeById(showtimeId)).thenReturn(showtime);

        ResultActions result = mockMvc.perform(get("/showtimes/{showtimeId}", showtimeId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(showtime.getId()))
                .andExpect(jsonPath("$.theater").value(showtime.getTheater()))
                .andExpect(jsonPath("$.startTime").value(showtime.getStartTime().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.endTime").value(showtime.getEndTime().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.price").value(showtime.getPrice()));
    }
    
    @Test
    public void ShowtimeController_CreateShowtime_SuccessTest() throws Exception {

        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
                .movieId(1L) // Assuming a movie with ID 1 exists
                .theater("Theater 1")
                .startTime(LocalDateTime.of(2023, 10, 1, 12, 0))
                .endTime(LocalDateTime.of(2023, 10, 1, 14, 0))
                .price(12.50)
                .build();

        Movie movie = Movie.builder()
                .id(1L)
                .title("Inception")
                .releaseYear(2010)
                .genre("Sci-Fi")
                .rating(8.8)
                .build();

        Showtime showtime = Showtime.builder()
                .id(1L)
                .movie(movie) 
                .theater(showtimeRequest.getTheater())
                .startTime(showtimeRequest.getStartTime())
                .endTime(showtimeRequest.getEndTime())
                .price(showtimeRequest.getPrice())
                .build();

        // Mock the service method to return the created showtime
        when(showtimeService.createShowtime(ArgumentMatchers.any())).thenReturn(showtime);

        // Perform the POST request and verify the response
        ResultActions result = mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtimeRequest)));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(showtime.getId()))
                .andExpect(jsonPath("$.theater").value(showtime.getTheater()))
                .andExpect(jsonPath("$.startTime").value(showtime.getStartTime().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.endTime").value(showtime.getEndTime().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
                .andExpect(jsonPath("$.price").value(showtime.getPrice()))
                .andExpect(jsonPath("$.movieId").value(showtime.getMovie().getId()));
    }

    @Test
    public void ShowtimeController_CreateShowtime_AlreadyExist_FailureTest() throws Exception {

        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
                .movieId(1L) // Assuming a movie with ID 1 exists
                .theater("Theater 1")
                .startTime(LocalDateTime.of(2023, 10, 1, 12, 0))
                .endTime(LocalDateTime.of(2023, 10, 1, 14, 0))
                .price(12.50)
                .build();

        // Mock the service method to throw an exception
        when(showtimeService.createShowtime(ArgumentMatchers.any())).thenThrow(new RuntimeException("Showtime already exists"));

        // Perform the POST request and verify the response
        ResultActions result = mockMvc.perform(post("/showtimes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtimeRequest)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Showtime already exists"));
    }

    @Test
    public void ShowtimeController_UpdateShowtime_SuccessTest() throws Exception{

        Showtime showtime = Showtime.builder()
                .id(1L)
                .theater("Theater 1")
                .startTime(LocalDateTime.of(2023, 10, 1, 12, 0))
                .endTime(LocalDateTime.of(2023, 10, 1, 14, 0))
                .price(12.50)
                .build();

        when(showtimeService.updateShowtime(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenReturn(showtime);

        ResultActions result = mockMvc.perform(post("/showtimes/update/{showtimeId}", showtime.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtime)));

        result.andExpect(status().isOk());
    }

    @Test
    public void ShowtimeController_UpdateShowtime_NotExists_FailureTest() throws Exception{

        Showtime showtime = Showtime.builder()
                .id(1L)
                .theater("Theater 1")
                .startTime(LocalDateTime.of(2023, 10, 1, 12, 0))
                .endTime(LocalDateTime.of(2023, 10, 1, 14, 0))
                .price(12.50)
                .build();

        when(showtimeService.updateShowtime(ArgumentMatchers.anyLong(), ArgumentMatchers.any())).thenThrow(new RuntimeException("Showtime not found"));

        ResultActions result = mockMvc.perform(post("/showtimes/update/{showtimeId}", showtime.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(showtime)));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Showtime not found"));
    }

    @Test
    public void ShowtimeController_DeleteShowtime_SuccessTest() throws Exception{

        Long showtimeId = 1L;

        doNothing().when(showtimeService).deleteShowtime(ArgumentMatchers.anyLong());

        ResultActions result = mockMvc.perform(delete("/showtimes/{showtimeId}", showtimeId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    public void ShowtimeController_DeleteShowtime_NotExists_FailureTest() throws Exception{

        Long showtimeId = 1L;

        doThrow(new RuntimeException("Showtime not found")).when(showtimeService).deleteShowtime(ArgumentMatchers.anyLong());

        ResultActions result = mockMvc.perform(delete("/showtimes/{showtimeId}", showtimeId)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Showtime not found"));
    }
}
