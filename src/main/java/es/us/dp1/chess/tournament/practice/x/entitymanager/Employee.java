package es.us.dp1.chess.tournament.practice.x.entitymanager;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee extends BaseEntity {

    @NotBlank
    String name;

    long salary;

}
