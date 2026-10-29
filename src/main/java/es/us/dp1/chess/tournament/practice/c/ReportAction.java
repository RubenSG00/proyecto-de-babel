package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDate;
import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReportAction extends BaseEntity {

    @NotNull
    @Size(min = 10, max = 200)
    String actionTaken;

    @NotNull
    @Enumerated(EnumType.STRING)
    ActionStatus status;

    @NotNull
    LocalDate deadline;

    LocalDateTime closedAt;

    @NotNull
    @Min(1)
    @Max(3)
    Integer priority;

    // Owning side of the bidirectional relation (the "many" side holds the FK)
    @NotNull
    @ManyToOne(optional = false)
    SafetyReport report;

    @ManyToOne
    User assignee;

}
