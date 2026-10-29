package es.us.dp1.chess.tournament.practice.x.relationships;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Athlete extends BaseEntity {

    @NotBlank
    String name;

    @Transient
    SportsLicense license;

}
