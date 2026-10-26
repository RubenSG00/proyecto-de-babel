package es.us.dp1.chess.tournament.match;

import java.util.List;
import java.util.Optional;

import es.us.dp1.chess.tournament.exceptions.InvalidWinnerException;

public class ChessMatchService {

    ChessMatchRepository repo;

    public ChessMatchService(ChessMatchRepository repo) {
        this.repo = repo;
    }

    public List<ChessMatch> findAll() {
        // TODO: Implement to solve Test 5
        return null;
    }

    public Optional<ChessMatch> findById(Integer id) {
        // TODO: Implement to solve Test 5
        return null;
    }

    public ChessMatch save(ChessMatch match) throws InvalidWinnerException {
        // TODO: Implement to solve Test 5
        return null;
    }

}
