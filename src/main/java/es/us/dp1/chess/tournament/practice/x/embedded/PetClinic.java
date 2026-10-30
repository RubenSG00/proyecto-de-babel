package es.us.dp1.chess.tournament.practice.x.embedded;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PetClinic extends BaseEntity {

    @NotBlank
    String name;

    // @Valid cascades the validation to the constraints of the embedded object
    @NotNull
    @Valid
    @Embedded
    PostalAddress address;

}
