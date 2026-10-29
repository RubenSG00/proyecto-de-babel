package es.us.dp1.chess.tournament.practice.a;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class NoticeAcknowledgement extends BaseEntity {

    // Optional: no @NotNull, so null stays valid; @Size only limits a present value
    @Size(max = 150)
    String comment;

    @NotNull
    @Enumerated(EnumType.STRING)
    AcknowledgementStatus status;

    @NotNull
    @Past
    LocalDateTime acknowledgedAt;

    @NotNull
    @ManyToOne(optional = false)
    ProgramNotice notice;

    @NotNull
    @ManyToOne(optional = false)
    User member;

    // Navigable end "0..1": optional (the default), so neither optional = false nor @NotNull
    @ManyToOne
    User delegate;

}
