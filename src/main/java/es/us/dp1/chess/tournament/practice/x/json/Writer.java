package es.us.dp1.chess.tournament.practice.x.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    // @JsonIgnore on the collection side breaks the loop writer -> novels -> writer -> ...;
    // a novel still shows its writer, and the novels of a writer have their own endpoint
    @JsonIgnore
    @OneToMany(mappedBy = "writer")
    List<Novel> novels;

}
