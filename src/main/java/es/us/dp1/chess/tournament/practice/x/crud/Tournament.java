package es.us.dp1.chess.tournament.practice.x.crud;

import java.math.BigDecimal;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Tournament extends BaseEntity {

    @NotNull
    @Size(min = 3, max = 50)
    String name;

    @NotBlank
    String description;

    String logoUrl;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal prizePool;

    @NotNull
    @Enumerated(EnumType.STRING)
    TournamentFormat format;

}
