package es.us.dp1.chess.tournament.practice.x.relationships;

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
public class Lesson extends BaseEntity {

    @NotBlank
    String topic;

    Integer position;

    // Owning side of the bidirectional relation (FK course_id)
    @NotNull
    @ManyToOne(optional = false)
    Course course;

}
