package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookLoan extends BaseEntity {

    @NotBlank
    String bookTitle;

    LocalDate loanDate;

    LocalDate dueDate;

    LocalDate returnDate;

    @NotNull
    @ManyToOne(optional = false)
    User borrower;

    @ManyToOne
    User recommender;

}
