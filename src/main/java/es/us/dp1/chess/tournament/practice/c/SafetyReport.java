package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SafetyReport {

    String code;

    String title;

    String description;

    Severity severity;

    LocalDateTime reportedAt;

    Integer delayMinutes;

    @Transient
    Flight flight;

    @Transient
    User reporter;

    @Transient
    List<User> witnesses;

    @Transient
    List<ReportAction> actions;

}
