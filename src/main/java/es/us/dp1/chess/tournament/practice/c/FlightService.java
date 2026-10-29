package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class FlightService {

    FlightRepository repo;

    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    public List<Flight> findAll() {
        // TODO: Implement to solve Test C5
        return null;
    }

    public Optional<Flight> findById(Integer id) {
        // TODO: Implement to solve Test C5
        return null;
    }

    public Flight save(Flight flight) throws InvalidCrewException, InvalidFlightTimesException {
        // TODO: Implement to solve Test C5
        return null;
    }

    public Flight registerLanding(Integer flightId, LocalDateTime arrival, boolean diverted)
            throws FlightAlreadyLandedException, InvalidFlightTimesException {
        // TODO: Implement to solve Test C5
        return null;
    }

}
