package es.us.dp1.chess.tournament.practice.x.transactions;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Wallet extends BaseEntity {

    @NotBlank
    String owner;

    @NotNull
    @Min(0)
    Integer balance;

}
