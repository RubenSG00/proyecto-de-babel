package es.us.dp1.chess.tournament.challenge;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    // Navigable end "1": mandatory for JPA (optional = false) and for validation (@NotNull)
    @NotNull
    @ManyToOne(optional = false)
    FriendlyChallenge challenge;

    // Navigable end "2..n": an announcement has many recipients and a user receives many
    // announcements, so it is many-to-many; the minimum of 2 is a validation constraint
    @NotNull
    @Size(min = 2)
    @ManyToMany
    List<User> recipients;

}
