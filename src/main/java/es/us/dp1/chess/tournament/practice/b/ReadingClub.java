package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReadingClub extends BaseEntity {

    @NotBlank
    String name;

    LocalDate foundedOn;

    @NotNull
    @Min(2)
    Integer maxMembers;

    @NotNull
    @Size(min = 2)
    @ManyToMany
    @JoinTable(name = "reading_club_members",
        joinColumns = @JoinColumn(name = "reading_club_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> members;

    @ManyToMany
    @JoinTable(name = "reading_club_loans",
        joinColumns = @JoinColumn(name = "reading_club_id"),
        inverseJoinColumns = @JoinColumn(name = "book_loan_id"))
    List<BookLoan> loans;

}
