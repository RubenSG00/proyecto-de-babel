package es.us.dp1.chess.tournament.practice.x.inheritance;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Car extends Vehicle {

    @NotNull
    @Min(2)
    @Max(5)
    Integer doors;

}
