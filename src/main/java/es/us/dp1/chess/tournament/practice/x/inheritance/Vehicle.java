package es.us.dp1.chess.tournament.practice.x.inheritance;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// @MappedSuperclass, not @Entity: no table for vehicles; its attributes (and their constraints)
// become columns of the table of each subclass
@MappedSuperclass
@Getter
@Setter
public abstract class Vehicle extends BaseEntity {

    @NotNull
    @Pattern(regexp = "[0-9]{4}[A-Z]{3}")
    String plate;

    @NotBlank
    String brand;

}
