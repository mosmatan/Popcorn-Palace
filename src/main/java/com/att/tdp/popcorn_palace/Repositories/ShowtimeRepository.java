package com.att.tdp.popcorn_palace.Repositories;

import com.att.tdp.popcorn_palace.Entities.Showtime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

}
