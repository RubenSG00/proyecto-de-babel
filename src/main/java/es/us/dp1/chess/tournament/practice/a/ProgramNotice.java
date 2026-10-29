package es.us.dp1.chess.tournament.practice.a;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramNotice {

    String text;

    NoticePriority priority;

    LocalDateTime publishedAt;

    Integer readCount;

    @Transient
    TrainingProgram program;

    @Transient
    List<User> targets;

}
