package es.us.dp1.chess.tournament.practice.x.json;

import java.util.List;

import es.us.dp1.chess.tournament.model.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Writer extends BaseEntity {

    @NotBlank
    String name;

    @OneToMany(mappedBy = "writer")
    List<Novel> novels;

}
