package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class ReadingClubService {

    private final ReadingClubRepository repo;
    private final UserService userService;

    @Autowired
    public ReadingClubService(ReadingClubRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<ReadingClub> findAll() {
        return (List<ReadingClub>) repo.findAll();
    }

    @Transactional(readOnly = true)
    public ReadingClub findById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ReadingClub", "id", id));
    }

    // Checks in the order of the statement: existence (404), membership or ADMIN (403), loans (404)
    @Transactional(readOnly = true)
    public List<OverdueEntry> getOverdueReport(Integer clubId) {
        ReadingClub club = findById(clubId);
        User current = userService.findCurrentUser();
        // User does not override equals: compare ids, not instances
        boolean member = club.getMembers().stream().anyMatch(m -> m.getId().equals(current.getId()));
        if (!member && !current.hasAuthority("ADMIN"))
            throw new AccessDeniedException("Only the members of the club and the administrators can see its report");
        if (club.getLoans() == null || club.getLoans().isEmpty())
            throw new ResourceNotFoundException("The club " + clubId + " has no loans");
        return repo.findOverdueCounts(clubId, LocalDate.now()).stream()
            .map(row -> new OverdueEntry((User) row[0], ((Number) row[1]).longValue()))
            .toList();
    }

}
