package es.us.dp1.chess.tournament.challenge;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChallengeAnnouncement {

    String message;

    AnnouncementStatus status;

    LocalDateTime sentAt;

    @Transient
    FriendlyChallenge challenge;

    @Transient
    List<User> recipients;

}
