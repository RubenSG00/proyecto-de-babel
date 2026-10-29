package es.us.dp1.chess.tournament.practice.x.inheritance;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Motorbike extends BaseEntity {

    String plate;

    String brand;

    Integer engineCc;

}
