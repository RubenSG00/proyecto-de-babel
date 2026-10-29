package es.us.dp1.chess.tournament.practice.a;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingSessionService {

    TrainingSessionRepository repo;

    @Autowired
    public TrainingSessionService(TrainingSessionRepository repo) {
        this.repo = repo;
    }

    // readOnly: the method only reads, so the persistence provider can skip dirty checking
    @Transactional(readOnly = true)
    public List<TrainingSession> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<TrainingSession> findById(Integer id) {
        return repo.findById(id);
    }

    // InvalidSessionScheduleException is checked: without rollbackFor the transaction would commit
    @Transactional(rollbackFor = InvalidSessionScheduleException.class)
    public TrainingSession save(TrainingSession session) throws InvalidSessionScheduleException {
        if (session.getFinish() != null
                && (session.getStart() == null || !session.getFinish().isAfter(session.getStart())))
            throw new InvalidSessionScheduleException();
        return repo.save(session);
    }

}
