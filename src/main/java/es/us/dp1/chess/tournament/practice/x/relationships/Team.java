package es.us.dp1.chess.tournament.practice.x.relationships;

import java.util.ArrayList;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Team extends BaseEntity {

    @NotBlank
    String name;

    // Aggregation: every cascade except REMOVE, so saving a team saves its new athletes but
    // deleting it does not delete them (an athlete can belong to several teams)
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    List<Athlete> athletes = new ArrayList<>();

}
