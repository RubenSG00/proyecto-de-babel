package es.us.dp1.chess.tournament.practice.x.errors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

@Service
public class VenueService {

    private final VenueRepository repo;

    @Autowired
    public VenueService(VenueRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public Venue findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venue", "id", id));
    }

    @Transactional(readOnly = true)
    public Venue checkAvailability(Integer id, int people) {
        Venue venue = findById(id);
        if (people > venue.getCapacity())
            throw new VenueCapacityExceededException(venue);
        return venue;
    }

    public Venue saveVenue(Venue venue) throws DuplicatedVenueNameException {
        // TODO: Implement to solve Test X16
        return null;
    }

}
