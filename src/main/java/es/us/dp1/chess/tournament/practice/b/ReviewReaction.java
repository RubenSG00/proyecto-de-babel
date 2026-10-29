package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewReaction {

    ReactionKind kind;

    LocalDateTime reactedAt;

    String note;

    @Transient
    BookReview review;

    @Transient
    User reactor;

}
