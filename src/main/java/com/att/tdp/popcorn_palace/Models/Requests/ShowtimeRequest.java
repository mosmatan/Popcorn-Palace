package com.att.tdp.popcorn_palace.Models.Requests;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ShowtimeRequest {

    private double price;
    private String theater;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long movieId;
}