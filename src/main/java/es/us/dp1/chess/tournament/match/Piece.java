package es.us.dp1.chess.tournament.match;

import com.fasterxml.jackson.annotation.JsonIgnore;

import es.us.dp1.chess.tournament.model.BaseEntity;

import java.lang.Cloneable;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of={"id"})
public class Piece extends BaseEntity implements Cloneable{

    /*
    @Min(1)
    @Max(8)
    Integer xPosition;

    @Min(1)
    @Max(8)
    Integer yPosition;
    */

    @OneToOne
    Position position;

    @ManyToOne()
    @JsonIgnore
    ChessBoard board;

    @ManyToOne()    
    private PieceType type;

    @Enumerated(EnumType.STRING)
    PieceColor color;

    Boolean captured;

    public Piece clone() {
        Piece piece = new Piece();
        piece.setPosition(this.getPosition());
        //piece.setYPosition(this.getYPosition());
        piece.setType(this.getType());
        piece.setColor(this.getColor());
        return piece;
    }


}
