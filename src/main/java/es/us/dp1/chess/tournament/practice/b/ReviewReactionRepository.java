package es.us.dp1.chess.tournament.practice.b;

import java.util.Optional;

public interface ReviewReactionRepository {
    Optional<ReviewReaction> findById(Integer id);
}
