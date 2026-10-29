package es.us.dp1.chess.tournament.practice.a;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.AccessDeniedException;
import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
import es.us.dp1.chess.tournament.user.User;
import es.us.dp1.chess.tournament.user.UserService;

@Service
public class TrainingProgramService {

    private final TrainingProgramRepository repo;
    private final UserService userService;

    @Autowired
    public TrainingProgramService(TrainingProgramRepository repo, UserService userService) {
        this.repo = repo;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<TrainingProgram> findAll() {
        return (List<TrainingProgram>) repo.findAll();
    }

    @Transactional(readOnly = true)
    public TrainingProgram findById(Integer id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", "id", id));
    }

    @Transactional
    public TrainingProgram save(TrainingProgram program) {
        return repo.save(program);
    }

    // Existence (404) -> membership (403) -> finished sessions (404): business rules of the service layer
    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getLeaderboard(Integer programId) {
        TrainingProgram program = findById(programId);
        User current = userService.findCurrentUser();
        // User does not override equals: compare ids, not instances
        boolean member = program.getMembers().stream().anyMatch(m -> m.getId().equals(current.getId()));
        if (!member)
            throw new AccessDeniedException("Only the members of the program can see its leaderboard");
        List<Object[]> points = repo.findMemberPoints(programId);
        if (points.isEmpty())
            throw new ResourceNotFoundException("No session of the program " + programId + " has finished yet");
        return points.stream()
            .map(row -> new LeaderboardEntry((User) row[0], ((Number) row[1]).longValue()))
            .toList();
    }

}
