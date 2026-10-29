package es.us.dp1.chess.tournament.practice.x.mapping;

import java.time.LocalDateTime;
import java.util.Date;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClinicVisit extends BaseEntity {

    LocalDateTime date;

    String description;

    Date petBirthDate;

}
