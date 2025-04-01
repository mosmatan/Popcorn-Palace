package com.att.tdp.popcorn_palace.Services;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import com.att.tdp.popcorn_palace.Repositories.MovieRepository;
import com.att.tdp.popcorn_palace.Repositories.ShowtimeRepository;
import com.att.tdp.popcorn_palace.Entities.Movie;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Models.Requests.ShowtimeRequest;

@ExtendWith(MockitoExtension.class)
public class ShowtimesServiceTests {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    @Test
    public void ShowtimeService_CreateShowtime_Crete1Showtime_SuccessTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .movieId(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(2))
            .build();
        
        Showtime existingShowtime1 = Showtime.builder()
            .theater("IMAX")
            .startTime(LocalDateTime.now().plusHours(-2))
            .endTime(LocalDateTime.now().plusHours(-1))
            .build(); 
        
        Showtime existingShowtime2 = Showtime.builder()
            .theater("IMAX")
            .startTime(LocalDateTime.now().plusHours(4))
            .endTime(LocalDateTime.now().plusHours(5))
            .build(); 

        when(showtimeRepository.findAll()
        .stream()
        .filter(st -> st.getTheater().equals(Mockito.anyString()))
        .toList()).thenReturn(List.of(
            existingShowtime1,
            existingShowtime2
        ));

        when(movieRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(new Movie()));
        when(showtimeRepository.save(Mockito.any(Showtime.class))).thenReturn(new Showtime());

        Showtime createdShowtime = assertDoesNotThrow(() -> showtimeService.createShowtime(showtimeRequest)); 

        assertNotNull(createdShowtime);
    }

    @Test
    public void ShowtimeService_CreateShowtime_OverlappingShowtimeSameTime_FailureTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .movieId(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .build();

        Showtime existingShowtimeSameTime = Showtime.builder()
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .build();
        

        when(showtimeRepository.findAll()
            .stream()
            .filter(st -> st.getTheater().equals(Mockito.anyString()))
            .toList()).thenReturn(List.of(existingShowtimeSameTime));


        Assertions.assertThatThrownBy(() -> showtimeService.createShowtime(showtimeRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cannot create/update showtime: Overlaps with existing showtime!");
    }

    @Test
    public void ShowtimeService_CreateShowtime_OverlappingShowtimeContains_FailureTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .movieId(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .build();

        
        Showtime existingShowtime = Showtime.builder()
            .theater("IMAX")
            .startTime(LocalDateTime.now().plusHours(1))
            .endTime(LocalDateTime.now().plusHours(2))
            .build();
        

        when(showtimeRepository.findAll()
            .stream()
            .filter(st -> st.getTheater().equals(Mockito.anyString()))
            .toList()).thenReturn(List.of(existingShowtime));


        Assertions.assertThatThrownBy(() -> showtimeService.createShowtime(showtimeRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cannot create/update showtime: Overlaps with existing showtime!");
    }

    @Test
    public void ShowtimeService_CreateShowtime_OverlappingShowtimeStartBefore_FailureTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .movieId(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .build();

        
        Showtime existingShowtime = Showtime.builder()
            .theater("IMAX")
            .startTime(LocalDateTime.now().plusHours(-1))
            .endTime(LocalDateTime.now().plusHours(2))
            .build();
        

        when(showtimeRepository.findAll()
            .stream()
            .filter(st -> st.getTheater().equals(Mockito.anyString()))
            .toList()).thenReturn(List.of(existingShowtime));


        Assertions.assertThatThrownBy(() -> showtimeService.createShowtime(showtimeRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cannot create/update showtime: Overlaps with existing showtime!");
    }

    @Test
    public void ShowtimeService_CreateShowtime_OverlappingShowtimeStartAfter_FailureTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .movieId(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .build();

        
        Showtime existingShowtime = Showtime.builder()
            .theater("IMAX")
            .startTime(LocalDateTime.now().plusHours(1))
            .endTime(LocalDateTime.now().plusHours(4))
            .build();
        

        when(showtimeRepository.findAll()
            .stream()
            .filter(st -> st.getTheater().equals(Mockito.anyString()))
            .toList()).thenReturn(List.of(existingShowtime));


        Assertions.assertThatThrownBy(() -> showtimeService.createShowtime(showtimeRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Cannot create/update showtime: Overlaps with existing showtime!");
    }

    @Test
    public void ShowtimeService_CreateShowtime_MovieNotFound_FailureTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .movieId(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .build();
        
        when(showtimeRepository.findAll()
            .stream()
            .filter(st -> st.getTheater().equals(Mockito.anyString()))
            .toList()).thenReturn(List.of());

        when(movieRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThatThrownBy(() -> showtimeService.createShowtime(showtimeRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("MovieId not found!");
    }

    @Test
    public void  ShowtimeService_UpdateShowtime_SuccessTest()
    {
        Movie movie = new Movie();
        movie.setId(1L);

        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .theater("IMAX2")
            .startTime(LocalDateTime.now().plusHours(1))
            .endTime(LocalDateTime.now().plusHours(4))
            .price(20)
            .movieId(1L)
            .build();

        Showtime updateShowtime = Showtime.builder()
            .id(1L)
            .theater("IMAX2")
            .startTime(LocalDateTime.now().plusHours(1))
            .endTime(LocalDateTime.now().plusHours(4))
            .price(20)
            .build();

        Showtime existingShowtime = Showtime.builder()
            .id(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .price(10)
            .build();
        
        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(existingShowtime));
        when(showtimeRepository.save(Mockito.any(Showtime.class))).thenReturn(updateShowtime);

        when(movieRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(movie));

        Showtime updatedShowtime = assertDoesNotThrow(() -> showtimeService.updateShowtime(1L, showtimeRequest)); 

        assertNotNull(updatedShowtime);
        Assertions.assertThat(updatedShowtime.getTheater()).isEqualTo(updateShowtime.getTheater());
        Assertions.assertThat(updatedShowtime.getStartTime()).isEqualTo(updateShowtime.getStartTime());
        Assertions.assertThat(updatedShowtime.getEndTime()).isEqualTo(updateShowtime.getEndTime());
        Assertions.assertThat(updatedShowtime.getPrice()).isEqualTo(updateShowtime.getPrice());
        Assertions.assertThat(updatedShowtime.getId()).isEqualTo(updateShowtime.getId());
    }

    @Test
    public void  ShowtimeService_UpdateShowtime_CantFindShowtimeById_FailureTest()
    {
        ShowtimeRequest showtimeRequest = ShowtimeRequest.builder()
            .build();

        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThatThrownBy(() -> showtimeService.updateShowtime(1L, showtimeRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Showtime not found!");
    }

    @Test
    public void ShowtimeService_GetShowtimeById_SuccessTest()
    {
        Showtime showtime = Showtime.builder()
            .id(1L)
            .theater("IMAX")
            .startTime(LocalDateTime.now())
            .endTime(LocalDateTime.now().plusHours(3))
            .price(10)
            .build();

        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(showtime));

        Showtime fetchedShowtime = assertDoesNotThrow(() -> showtimeService.getShowtimeById(1L)); 

        assertNotNull(fetchedShowtime);
        Assertions.assertThat(fetchedShowtime.getTheater()).isEqualTo(showtime.getTheater());
        Assertions.assertThat(fetchedShowtime.getStartTime()).isEqualTo(showtime.getStartTime());
        Assertions.assertThat(fetchedShowtime.getEndTime()).isEqualTo(showtime.getEndTime());
        Assertions.assertThat(fetchedShowtime.getPrice()).isEqualTo(showtime.getPrice());
        Assertions.assertThat(fetchedShowtime.getId()).isEqualTo(showtime.getId());
    }

    @Test
    public void ShowtimeService_GetShowtimeById_CantFindShowtimeById_FailureTest()
    {
        when(showtimeRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.empty());

        Assertions.assertThatThrownBy(() -> showtimeService.getShowtimeById(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Showtime not found!");
    }

    @Test
    public void ShowtimeService_DeleteShowtime_SuccessTest()
    {
        when(showtimeRepository.existsById(Mockito.anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> showtimeService.deleteShowtime(1L)); 
    }

    @Test
    public void ShowtimeService_DeleteShowtime_CantFindShowtimeById_FailureTest()
    {
        when(showtimeRepository.existsById(Mockito.anyLong())).thenReturn(false);

        Assertions.assertThatThrownBy(() -> showtimeService.deleteShowtime(1L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Showtime not found!");
    }
}

