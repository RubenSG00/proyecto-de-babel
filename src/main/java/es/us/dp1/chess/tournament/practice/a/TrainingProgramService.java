package es.us.dp1.chess.tournament.practice.a;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
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

    @Transactional(readOnly = true)
    public List<LeaderboardEntry> getLeaderboard(Integer programId) {
        // TODO: Implement to solve Test A7
        return null;
    }

}
