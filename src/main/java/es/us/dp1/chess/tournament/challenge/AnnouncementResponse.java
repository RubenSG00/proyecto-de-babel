package es.us.dp1.chess.tournament.challenge;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AnnouncementResponse extends BaseEntity {

    @NotNull
    @Size(min = 5, max = 200)
    String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    ResponseStatus status;

    @NotNull
    LocalDateTime respondedAt;

    @NotNull
    @ManyToOne(optional = false)
    ChallengeAnnouncement announcement;

    @NotNull
    @ManyToOne(optional = false)
    User recipient;

}
