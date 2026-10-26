package es.us.dp1.chess.tournament.match;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ChessMatchRepository extends CrudRepository<ChessMatch, Integer> {
    public List<ChessMatch> findAll();
}
