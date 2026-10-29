package es.us.dp1.chess.tournament.practice.x.errors;

import java.util.Optional;

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

    // The exception is checked: without rollbackFor the transaction would commit
    @Transactional(rollbackFor = DuplicatedVenueNameException.class)
    public Venue saveVenue(Venue venue) throws DuplicatedVenueNameException {
        Optional<Venue> sameName = repo.findByNameIgnoreCase(venue.getName());
        // Another venue (different id) with the same name; the venue itself may keep its name
        if (sameName.isPresent() && !sameName.get().getId().equals(venue.getId()))
            throw new DuplicatedVenueNameException(venue.getName());
        return repo.save(venue);
    }

}
