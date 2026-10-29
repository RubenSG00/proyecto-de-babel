package es.us.dp1.chess.tournament.practice.x.crud;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TournamentRepository extends CrudRepository<Tournament, Integer> {
    List<Tournament> findAll();
}
