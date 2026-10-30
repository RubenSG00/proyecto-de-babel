package es.us.dp1.chess.tournament.practice.x.inheritance;

import es.us.dp1.chess.tournament.model.NamedEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

// id and name (and its @Size) come from the mapped superclass NamedEntity
@Entity
@Getter
@Setter
public class Studio extends NamedEntity {

    @NotNull
    @Min(1)
    Integer rooms;

}
