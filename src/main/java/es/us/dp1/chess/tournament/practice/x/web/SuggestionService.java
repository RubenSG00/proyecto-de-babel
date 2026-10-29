package es.us.dp1.chess.tournament.practice.x.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SuggestionService {

    private final SuggestionRepository repo;

    @Autowired
    public SuggestionService(SuggestionRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Suggestion> findAll() {
        return repo.findAll();
    }

    @Transactional
    public Suggestion save(Suggestion suggestion) {
        return repo.save(suggestion);
    }

}
