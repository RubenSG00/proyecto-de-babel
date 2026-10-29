package es.us.dp1.chess.tournament.practice.x.web;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SuggestionRepository extends CrudRepository<Suggestion, Integer> {
    List<Suggestion> findAll();
}
