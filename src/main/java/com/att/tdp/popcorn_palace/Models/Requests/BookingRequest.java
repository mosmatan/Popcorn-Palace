package com.att.tdp.popcorn_palace.Models.Requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BookingRequest {
    private long showtimeId;
    private int seatNumber;
    private String userId;
}
