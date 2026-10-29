package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Column;
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
public class Flight extends BaseEntity {

    @NotBlank
    @Column(unique = true)
    String code;

    LocalDateTime departure;

    LocalDateTime arrival;

    @NotNull
    @Min(1)
    Integer distanceKm;

    @NotNull
    Boolean diverted;

    @ManyToOne
    User captain;

    @ManyToOne
    User firstOfficer;

}
