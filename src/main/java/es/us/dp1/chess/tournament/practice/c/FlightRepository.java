package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Integer> {
    public List<Flight> findAll();
}
