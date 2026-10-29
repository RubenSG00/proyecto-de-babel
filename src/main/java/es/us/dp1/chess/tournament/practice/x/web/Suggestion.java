package es.us.dp1.chess.tournament.practice.x.web;

import java.time.LocalDateTime;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Suggestion extends BaseEntity {

    @NotBlank
    @Size(min = 5, max = 80)
    String title;

    @NotBlank
    @Size(max = 500)
    String text;

    LocalDateTime createdAt;

    @ManyToOne
    User author;

}
