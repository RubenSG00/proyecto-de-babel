package es.us.dp1.chess.tournament.practice.x.errors;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface VenueRepository extends CrudRepository<Venue, Integer> {
    Optional<Venue> findByNameIgnoreCase(String name);
}
