package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;
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

    public List<PilotScore> getPilotRanking(Integer baseId, Integer minScore) {
        // TODO: Implement to solve Test C7
        return null;
    }

}
