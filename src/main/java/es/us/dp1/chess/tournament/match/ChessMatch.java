package es.us.dp1.chess.tournament.match;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.NamedEntity;
import es.us.dp1.chess.tournament.user.User;

import java.lang.Cloneable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class ChessMatch extends NamedEntity implements Cloneable {
    LocalDateTime start;
    LocalDateTime finish;
    Long turnDuration;

    ChessMatchType type;

    @ManyToOne
    User white;

    @ManyToOne
    User black;

    @ManyToOne
    User winner;

    @OneToOne(cascade = CascadeType.ALL)
    ChessBoard board;

    public ChessMatch clone() {
        ChessMatch match = new ChessMatch();
        match.setName(this.getName());
        match.setStart(this.getStart());
        match.setFinish(this.getFinish());
        match.setTurnDuration(this.getTurnDuration());
        match.setType(this.getType());
        match.setWhite(this.getWhite());
        match.setBlack(this.getBlack());
        match.setWinner(this.getWinner());
        match.setBoard(getBoard().clone());
        return match;
    }

}
