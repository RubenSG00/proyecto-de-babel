package es.us.dp1.chess.tournament.practice.x.queries;

import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Musician extends BaseEntity {

    @NotBlank
    String name;

    @NotBlank
    String instrument;

    @NotNull
    Integer salary;

    String agent;

    @ManyToMany
    List<Band> bands;

}
