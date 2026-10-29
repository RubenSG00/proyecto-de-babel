package es.us.dp1.chess.tournament.practice.b;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface BookReviewRepository extends CrudRepository<BookReview, Integer> {
    Optional<BookReview> findById(Integer id);
}
