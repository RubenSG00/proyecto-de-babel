package es.us.dp1.chess.tournament.practice.a;

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
public class TrainingProgram extends BaseEntity {

    @NotBlank
    String name;

    @NotNull
    LocalDate startDate;

    @NotNull
    LocalDate endDate;

    @NotNull
    @Min(1)
    Integer plannedSessions;

    @ManyToMany
    @JoinTable(name = "training_program_sessions",
        joinColumns = @JoinColumn(name = "training_program_id"),
        inverseJoinColumns = @JoinColumn(name = "training_session_id"))
    List<TrainingSession> sessions;

    @NotNull
    @Size(min = 2)
    @ManyToMany
    @JoinTable(name = "training_program_members",
        joinColumns = @JoinColumn(name = "training_program_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> members;

}
