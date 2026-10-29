package es.us.dp1.chess.tournament.practice.x.json;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Novel extends BaseEntity {

    @NotBlank
    String title;

    @NotNull
    @ManyToOne(optional = false)
    Writer writer;

}
