package es.us.dp1.chess.tournament.practice.x.web;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Recipe extends BaseEntity {

    @NotBlank
    String name;

    @NotNull
    @Min(1)
    Integer minutes;

    Boolean featured;

    @ManyToOne
    User owner;

}
