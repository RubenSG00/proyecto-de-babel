package es.us.dp1.chess.tournament.challenge;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnnouncementResponse {

    String message;

    ResponseStatus status;

    LocalDateTime respondedAt;

    @Transient
    ChallengeAnnouncement announcement;

    @Transient
    User recipient;

}
