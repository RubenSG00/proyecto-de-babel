package es.us.dp1.chess.tournament.challenge;

import java.util.List;

import es.us.dp1.chess.tournament.match.ChessMatch;
import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FriendlyChallenge extends BaseEntity {

    @NotNull
    String startDate;

    @NotNull
    String endDate;

    @NotNull
    @Min(1)
    Integer totalMatches;

    @Min(0)
    Integer completedMatches;

    @ManyToMany
    @JoinTable(name = "friendly_challenge_matches",
        joinColumns = @JoinColumn(name = "friendly_challenge_id"),
        inverseJoinColumns = @JoinColumn(name = "chess_match_id"))
    List<ChessMatch> matches;

    @NotNull
    @Size(min = 2)
    @ManyToMany
    @JoinTable(name = "friendly_challenge_participants",
        joinColumns = @JoinColumn(name = "friendly_challenge_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> participants;

}
