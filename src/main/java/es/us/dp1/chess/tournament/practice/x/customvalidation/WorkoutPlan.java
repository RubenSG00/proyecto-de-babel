package es.us.dp1.chess.tournament.practice.x.customvalidation;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WorkoutPlan extends BaseEntity {

    @NotBlank
    String name;

    @NotNull
    @MultipleOf(5)
    Integer minutes;

}
