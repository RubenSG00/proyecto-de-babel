package es.us.dp1.chess.tournament.match;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class PieceType {

    @Id   
    private String name;  // Ejemplo: "King", "Queen", "Pawn"

    @Column(name = "points", nullable = false)
    private Integer value;  // Valor numérico (1000, 9, 5, ...)

    public PieceType() {}

    public PieceType(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return name + " (value: " + value + ")";
    }
}
