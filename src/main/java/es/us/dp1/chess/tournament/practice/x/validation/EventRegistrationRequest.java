package es.us.dp1.chess.tournament.practice.x.validation;

import java.util.List;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventRegistrationRequest {

    // @NotNull: "" is valid
    @NotNull
    String nickname;

    // @NotBlank: at least one non-whitespace character
    @NotBlank
    String displayName;

    // @NotNull: an empty list is valid
    @NotNull
    List<String> interests;

    // @NotEmpty: not null and at least one element
    @NotEmpty
    List<String> languages;

    @NotNull
    @Min(1)
    @Max(10)
    Integer seats;

    // Double, not double: a primitive can never be null, and @DecimalMin/@DecimalMax are not
    // supported on double/float according to the course (T5 p.81)
    @NotNull
    @DecimalMin("0.5")
    @DecimalMax("99.9")
    Double maxPrice;

}
