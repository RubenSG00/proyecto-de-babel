package es.us.dp1.chess.tournament.practice.x.mapping;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LoyaltyCard extends BaseEntity {

    @NotBlank
    String cardNumber;

    @NotBlank
    String holder;

}
