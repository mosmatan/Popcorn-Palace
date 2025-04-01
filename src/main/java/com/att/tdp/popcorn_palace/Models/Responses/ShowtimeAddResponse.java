package com.att.tdp.popcorn_palace.Models.Responses;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShowtimeAddResponse {

    private Long id;
    private double price;
    private String theater;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long movieId;
}
