package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class CrewBaseService {

    private final CrewBaseRepository repo;
    private final UserService userService;

    @Autowired
    public CrewBaseService(CrewBaseRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<CrewBase> findAll() {
        return (List<CrewBase>) repo.findAll();
    }

    @Transactional(readOnly = true)
    public CrewBase findById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CrewBase", "id", id));
    }

    // All the business rules live here, in the order of the statement; the controller only
    // translates HTTP into a call to this method (layered architecture)
    @Transactional(readOnly = true)
    public List<PilotScore> getPilotRanking(Integer baseId, Integer minScore) {
        if (minScore < 0)
            throw new InvalidScoreThresholdException(minScore);
        CrewBase base = findById(baseId);
        User current = userService.findCurrentUser();
        // User does not override equals: compare ids, not instances
        boolean pilot = base.getPilots().stream().anyMatch(p -> p.getId().equals(current.getId()));
        if (!pilot && !current.hasAuthority("ADMIN"))
            throw new AccessDeniedException("Only the pilots of the base and the administrators can see its ranking");
        // Decided from the flights, not from the query: a high minScore may give an empty 200
        boolean anyCounts = base.getFlights() != null && base.getFlights().stream()
            .anyMatch(f -> f.getArrival() != null && !Boolean.TRUE.equals(f.getDiverted()));
        if (!anyCounts)
            throw new ResourceNotFoundException("No flight of the base " + baseId + " has landed without being diverted");
        return repo.findPilotScores(baseId, minScore).stream()
            .map(row -> new PilotScore((User) row[0], ((Number) row[1]).longValue()))
            .toList();
    }

}
