package es.us.dp1.chess.tournament.practice.x.queries;

import java.time.LocalDate;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Concert extends BaseEntity {

    @NotBlank
    String title;

    @NotBlank
    String city;

    @NotNull
    LocalDate date;

    @NotNull
    @Min(0)
    Integer price;

    @NotNull
    Boolean soldOut;

    @NotNull
    @Enumerated(EnumType.STRING)
    MusicGenre genre;

}
