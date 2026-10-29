package es.us.dp1.chess.tournament.practice.a;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProgramNotice extends BaseEntity {

    // @NotBlank implies not null and rejects "      "; @Size alone would accept it
    @NotBlank
    @Size(min = 5, max = 80)
    String text;

    @NotNull
    @Enumerated(EnumType.STRING)
    NoticePriority priority;

    LocalDateTime publishedAt;

    // @Min accepts null (only the @Not... constraints reject it), so @NotNull is needed too
    @NotNull
    @Min(0)
    Integer readCount;

    @NotNull
    @ManyToOne(optional = false)
    TrainingProgram program;

    // Navigable end "1..n": many-to-many, and at least one element (@NotEmpty)
    @NotNull
    @NotEmpty
    @ManyToMany
    List<User> targets;

}
