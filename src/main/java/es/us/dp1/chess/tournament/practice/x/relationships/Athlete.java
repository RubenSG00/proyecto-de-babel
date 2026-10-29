package es.us.dp1.chess.tournament.practice.x.relationships;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Athlete extends BaseEntity {

    @NotBlank
    String name;

    // Mandatory one-to-one whose license is saved and deleted with its athlete
    @NotNull
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    SportsLicense license;

}
