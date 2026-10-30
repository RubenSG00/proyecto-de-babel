package es.us.dp1.chess.tournament.practice.x.embedded;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// A value object: no identity, its attributes are columns of the table of its owner
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class PostalAddress {

    @NotBlank
    String street;

    @NotBlank
    String city;

    @NotNull
    @Pattern(regexp = "[0-9]{5}")
    String zipCode;

}
