package es.us.dp1.chess.tournament.practice.x.security;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Expense extends BaseEntity {

    @NotBlank
    String description;

    @NotNull
    @Min(0)
    Integer amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    ExpenseStatus status;

    @NotNull
    @ManyToOne(optional = false)
    User owner;

    @ManyToOne
    User approvedBy;

}
