package es.us.dp1.chess.tournament.practice.c;

import java.time.LocalDateTime;
import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;
import es.us.dp1.chess.tournament.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SafetyReport extends BaseEntity {

    // @Pattern must match the whole string and accepts null (hence @NotNull).
    // Uniqueness is a database constraint, checked when the row is flushed, not by Bean Validation.
    @NotNull
    @Pattern(regexp = "SR-\\d{4}")
    @Column(unique = true)
    String code;

    @NotBlank
    @Size(min = 5, max = 60)
    String title;

    @Size(max = 500)
    String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    Severity severity;

    @NotNull
    @Past
    LocalDateTime reportedAt;

    // Optional: @Min/@Max accept null
    @Min(0)
    @Max(1440)
    Integer delayMinutes;

    @Transient
    Flight flight;

    @Transient
    User reporter;

    @Transient
    List<User> witnesses;

    @Transient
    List<ReportAction> actions;

}
