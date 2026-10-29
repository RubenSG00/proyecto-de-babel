package es.us.dp1.chess.tournament.practice.x.crud;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.ResourceNotFoundException;

@Service
public class TournamentService {

    private final TournamentRepository repo;

    @Autowired
    public TournamentService(TournamentRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Tournament> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Tournament findById(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tournament", "id", id));
    }

    @Transactional
    public Tournament save(Tournament tournament) {
        return repo.save(tournament);
    }

    // TODO: Test X13 (update) and Test X14 (delete)

}
