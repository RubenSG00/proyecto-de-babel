package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReviewReaction extends BaseEntity {

    @NotNull
    @Enumerated(EnumType.STRING)
    ReactionKind kind;

    @NotNull
    LocalDateTime reactedAt;

    @Size(max = 100)
    String note;

    @Transient
    BookReview review;

    @Transient
    User reactor;

}
