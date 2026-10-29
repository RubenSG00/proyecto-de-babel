package es.us.dp1.chess.tournament.practice.b;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ReviewReactionRepository extends CrudRepository<ReviewReaction, Integer> {
    Optional<ReviewReaction> findById(Integer id);
}
