package es.us.dp1.chess.tournament.practice.x.domain;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
public class Locker extends BaseEntity {

    @NotBlank
    String code;

    String building;

}
