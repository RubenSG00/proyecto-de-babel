package es.us.dp1.chess.tournament.practice.a;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TrainingSessionRepository extends CrudRepository<TrainingSession, Integer> {
    public List<TrainingSession> findAll();
}
