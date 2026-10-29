package es.us.dp1.chess.tournament.practice.x.relationships;

import java.util.ArrayList;
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
public class Course extends BaseEntity {

    @NotBlank
    String title;

    // Inverse side: mappedBy names the attribute of Lesson that owns the relation, so the
    // relation is only the FK course_id of the lesson table (no join table)
    @OneToMany(mappedBy = "course")
    List<Lesson> lessons = new ArrayList<>();

    // Only the owning side (Lesson.course) is stored: both sides are set to keep them coherent
    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
        lesson.setCourse(this);
    }

}
