package com.att.tdp.popcorn_palace.Repositories;

import com.att.tdp.popcorn_palace.Entities.Movie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Movies table.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>
{
    Optional<Movie> findByTitle(String movieTitle);
}
