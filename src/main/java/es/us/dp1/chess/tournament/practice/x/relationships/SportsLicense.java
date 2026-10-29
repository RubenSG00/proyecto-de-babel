package es.us.dp1.chess.tournament.practice.x.relationships;

import java.time.LocalDate;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SportsLicense extends BaseEntity {

    @NotBlank
    String number;

    LocalDate expiresOn;

}
