package es.us.dp1.chess.tournament.practice.x.transactions;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Enrollment extends BaseEntity {

    @NotBlank
    String attendee;

    @NotNull
    @ManyToOne(optional = false)
    Workshop workshop;

    @NotNull
    @OneToOne(optional = false)
    Receipt receipt;

}
