package es.us.dp1.chess.tournament.practice.x.inheritance;

import es.us.dp1.chess.tournament.model.Person;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

// id, first_name and last_name (and their constraints) come from the mapped superclass Person
@Entity
@Getter
@Setter
public class Instructor extends Person {

    @NotBlank
    String specialty;

}
