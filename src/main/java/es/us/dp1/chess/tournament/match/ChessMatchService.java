package es.us.dp1.chess.tournament.match;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.exceptions.InvalidWinnerException;
import es.us.dp1.chess.tournament.user.User;

@Service
public class ChessMatchService {

    ChessMatchRepository repo;

    @Autowired
    public ChessMatchService(ChessMatchRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<ChessMatch> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<ChessMatch> findById(Integer id) {
        return repo.findById(id);
    }

    // InvalidWinnerException is checked: Spring only rolls back unchecked exceptions by default
    @Transactional(rollbackFor = InvalidWinnerException.class)
    public ChessMatch save(ChessMatch match) throws InvalidWinnerException {
        User winner = match.getWinner();
        if (winner != null && !winner.equals(match.getWhite()) && !winner.equals(match.getBlack()))
            throw new InvalidWinnerException();
        return repo.save(match);
    }

}
