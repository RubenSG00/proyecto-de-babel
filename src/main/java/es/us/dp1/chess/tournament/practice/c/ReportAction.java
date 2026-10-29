package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDate;
import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportAction {

    String actionTaken;

    ActionStatus status;

    LocalDate deadline;

    LocalDateTime closedAt;

    Integer priority;

    @Transient
    SafetyReport report;

    @Transient
    User assignee;

}
