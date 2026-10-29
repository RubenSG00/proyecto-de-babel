package es.us.dp1.chess.tournament.practice.a;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrainingSession extends BaseEntity {

    LocalDateTime start;

    LocalDateTime finish;

    @Min(1)
    Integer durationMinutes;

    Boolean goalAchieved;

    @NotNull
    @ManyToOne(optional = false)
    User coach;

    @NotNull
    @ManyToOne(optional = false)
    User trainee;

}
