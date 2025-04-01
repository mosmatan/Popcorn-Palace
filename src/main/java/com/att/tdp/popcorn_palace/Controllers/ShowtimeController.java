package com.att.tdp.popcorn_palace.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.att.tdp.popcorn_palace.Services.ShowtimeService;
import com.att.tdp.popcorn_palace.Entities.Showtime;
import com.att.tdp.popcorn_palace.Models.ErrorResponseObject;
import com.att.tdp.popcorn_palace.Models.Requests.ShowtimeRequest;
import com.att.tdp.popcorn_palace.Models.Responses.ShowtimeAddResponse;
import com.att.tdp.popcorn_palace.Utils.Adapters;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService showtimeService;

    public ShowtimeController(ShowtimeService showtimeService) {
        this.showtimeService = showtimeService;
    }

    // GET /showtimes/{showtimeId}
    @GetMapping("/{showtimeId}")
    public ResponseEntity<Object> getShowtimeById(@PathVariable Long showtimeId) {
        try {
            Showtime entity = showtimeService.getShowtimeById(showtimeId);

            if (entity == null) {
                return ResponseEntity.notFound().build();
            }

            ShowtimeAddResponse response = Adapters.ToShowtimeAddResponse(entity);
            return ResponseEntity.ok(response);
        } 
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // POST /showtimes
    @PostMapping
    public ResponseEntity<Object> addShowtime(@RequestBody ShowtimeRequest showtime) {
        try {
            Showtime newShowtime = showtimeService.createShowtime(showtime); 
            
            ShowtimeAddResponse response = Adapters.ToShowtimeAddResponse(newShowtime);
            
            return ResponseEntity.ok(response);
        } 
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // POST /showtimes/update/{showtimeId}
    @PostMapping("/update/{showtimeId}")
    public ResponseEntity<Object> updateShowtimeById(
            @PathVariable Long showtimeId,
            @RequestBody ShowtimeRequest updatedShowtime
    ) {
        try {
            showtimeService.updateShowtime(showtimeId, updatedShowtime);
            return ResponseEntity.ok(null);
        } 
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // DELETE /showtimes/{showtimeId}
    @DeleteMapping("/{showtimeId}")
    public ResponseEntity<Object> deleteShowtimeById(@PathVariable Long showtimeId) {
        try {
            showtimeService.deleteShowtime(showtimeId);
            return ResponseEntity.ok(null);
        } 
        catch (RuntimeException e) {

            ErrorResponseObject errorResponse = new ErrorResponseObject(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
