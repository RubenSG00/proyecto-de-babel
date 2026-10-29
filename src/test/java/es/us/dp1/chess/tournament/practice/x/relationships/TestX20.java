package es.us.dp1.chess.tournament.practice.x.relationships;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@DataJpaTest
public class TestX20 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Autowired
    DataSource dataSource;

    @Test
    public void testX20Mappings() throws NoSuchFieldException {
        checkThatFieldIsAnnotatedWith(Lesson.class, "course", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(Lesson.class, "course", NotNull.class);
        checkThatFieldIsAnnotatedWith(Course.class, "lessons", OneToMany.class);
        assertEquals("course", Course.class.getDeclaredField("lessons").getAnnotation(OneToMany.class).mappedBy(),
                "Course.lessons must be the inverse side: mappedBy = \"course\"");
    }

    @Test
    public void testX20TheRelationIsAForeignKeyAndNoJoinTable() {
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        Integer fk = jdbc.queryForObject("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS "
                + "WHERE UPPER(TABLE_NAME) = 'LESSON' AND UPPER(COLUMN_NAME) = 'COURSE_ID'", Integer.class);
        Integer joinTable = jdbc.queryForObject("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES "
                + "WHERE UPPER(TABLE_NAME) = 'COURSE_LESSONS'", Integer.class);
        assertEquals(1, fk, "The table lesson must have the foreign key course_id");
        assertEquals(0, joinTable, "No join table may be created for this relation");
    }

    @Test
    public void testX20AddLessonKeepsBothSidesCoherent() {
        Course c = new Course();
        c.setTitle("Endgames");
        Lesson l = new Lesson();
        l.setTopic("Rook endings");
        l.setPosition(1);
        c.addLesson(l);
        assertTrue(c.getLessons().contains(l), "addLesson must add the lesson to the course");
        assertSame(c, l.getCourse(), "addLesson must set the course of the lesson");
    }

    @Test
    public void testX20BothSidesAreNavigableAfterSaving() {
        Course c = new Course();
        c.setTitle("Openings");
        em.persist(c);
        for (int i = 1; i <= 2; i++) {
            Lesson l = new Lesson();
            l.setTopic("Topic " + i);
            l.setPosition(i);
            c.addLesson(l);
            em.persist(l);
        }
        em.flush();
        em.clear();
        Course stored = em.find(Course.class, c.getId());
        assertEquals(2, stored.getLessons().size());
        assertEquals(c.getId(), stored.getLessons().get(0).getCourse().getId());
    }

}
