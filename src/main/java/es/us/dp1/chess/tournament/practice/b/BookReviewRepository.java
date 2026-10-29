package es.us.dp1.chess.tournament.practice.b;

import java.util.Optional;

public interface BookReviewRepository {
    Optional<BookReview> findById(Integer id);
}
