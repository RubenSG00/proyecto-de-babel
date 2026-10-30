package es.us.dp1.chess.tournament.practice.x.mapping;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LoyaltyCard extends BaseEntity {

    // A database constraint: a repeated number is detected when the row is flushed
    @NotBlank
    @Column(unique = true)
    String cardNumber;

    @NotBlank
    String holder;

}
