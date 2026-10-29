package es.us.dp1.chess.tournament.practice.a;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeAcknowledgement {

    String comment;

    AcknowledgementStatus status;

    LocalDateTime acknowledgedAt;

    @Transient
    ProgramNotice notice;

    @Transient
    User member;

    @Transient
    User delegate;

}
