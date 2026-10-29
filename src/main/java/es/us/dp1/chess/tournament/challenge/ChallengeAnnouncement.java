package es.us.dp1.chess.tournament.challenge;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChallengeAnnouncement extends BaseEntity {

    @NotNull
    @Size(min = 10, max = 60)
    String message;

    // STRING stores "DRAFT"/"SENT"/"ARCHIVED"; the default (ORDINAL) would store 0/1/2
    @NotNull
    @Enumerated(EnumType.STRING)
    AnnouncementStatus status;

    LocalDateTime sentAt;

    @Transient
    FriendlyChallenge challenge;

    @Transient
    List<User> recipients;

}
