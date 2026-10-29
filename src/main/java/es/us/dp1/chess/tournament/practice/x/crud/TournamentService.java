package es.us.dp1.chess.tournament.practice.x.crud;

import java.util.List;

import org.springframework.beans.BeanUtils;
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

    // Test X13: the stored tournament is modified (404 if it does not exist); the id of the
    // received object is ignored, so the id of the URL always wins
    @Transactional
    public Tournament update(Integer id, Tournament tournament) {
        Tournament toUpdate = findById(id);
        BeanUtils.copyProperties(tournament, toUpdate, "id");
        return repo.save(toUpdate);
    }

    // Test X14: look the tournament up first, so that an unknown id is a 404
    // (deleteById of a missing id does nothing in Spring Data 3)
    @Transactional
    public void delete(Integer id) {
        Tournament toDelete = findById(id);
        repo.delete(toDelete);
    }

}
