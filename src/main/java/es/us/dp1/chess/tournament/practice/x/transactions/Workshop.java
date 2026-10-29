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
public class Workshop extends BaseEntity {

    @NotBlank
    String name;

    @NotNull
    @Min(1)
    Integer capacity;

    @NotNull
    @Min(0)
    Integer enrolled;

}
