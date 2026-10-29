package es.us.dp1.chess.tournament.practice.a;

import java.util.List;
import java.util.Optional;

public class TrainingSessionService {

    TrainingSessionRepository repo;

    public TrainingSessionService(TrainingSessionRepository repo) {
        this.repo = repo;
    }

    public List<TrainingSession> findAll() {
        // TODO: Implement to solve Test A5
        return null;
    }

    public Optional<TrainingSession> findById(Integer id) {
        // TODO: Implement to solve Test A5
        return null;
    }

    public TrainingSession save(TrainingSession session) throws InvalidSessionScheduleException {
        // TODO: Implement to solve Test A5
        return null;
    }

}
