package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;

@Service
public class FlightService {

    FlightRepository repo;

    @Autowired
    public FlightService(FlightRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Flight> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Flight> findById(Integer id) {
        return repo.findById(id);
    }

    // Both business exceptions are checked: each one must be listed in rollbackFor
    @Transactional(rollbackFor = { InvalidCrewException.class, InvalidFlightTimesException.class })
    public Flight save(Flight flight) throws InvalidCrewException, InvalidFlightTimesException {
        checkCrew(flight);
        if (flight.getArrival() != null)
            checkTimes(flight.getDeparture(), flight.getArrival());
        return repo.save(flight);
    }

    // ResourceNotFoundException is unchecked and rolls back by default
    @Transactional(rollbackFor = { FlightAlreadyLandedException.class, InvalidFlightTimesException.class })
    public Flight registerLanding(Integer flightId, LocalDateTime arrival, boolean diverted)
            throws FlightAlreadyLandedException, InvalidFlightTimesException {
        Flight flight = repo.findById(flightId)
            .orElseThrow(() -> new ResourceNotFoundException("Flight", "id", flightId));
        if (flight.getArrival() != null)
            throw new FlightAlreadyLandedException();
        checkTimes(flight.getDeparture(), arrival);
        // The checks come before any change, so a rejected landing does not modify the flight
        flight.setArrival(arrival);
        flight.setDiverted(diverted);
        return repo.save(flight);
    }

    private void checkCrew(Flight flight) throws InvalidCrewException {
        User captain = flight.getCaptain();
        User firstOfficer = flight.getFirstOfficer();
        if (captain == null || firstOfficer == null || sameUser(captain, firstOfficer))
            throw new InvalidCrewException();
    }

    // The same object, or two objects with the same id, are the same user
    private boolean sameUser(User a, User b) {
        return a == b || (a.getId() != null && a.getId().equals(b.getId()));
    }

    private void checkTimes(LocalDateTime departure, LocalDateTime arrival) throws InvalidFlightTimesException {
        if (departure == null || arrival == null || !arrival.isAfter(departure))
            throw new InvalidFlightTimesException();
    }

}
