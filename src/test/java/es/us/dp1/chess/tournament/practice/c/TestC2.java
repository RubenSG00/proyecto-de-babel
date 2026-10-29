package es.us.dp1.chess.tournament.practice.c;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;

@DataJpaTest()
public class TestC2 extends ReflexiveTest {

    @Autowired(required = false)
    EntityManager em;

    @Autowired
    DataSource dataSource;

    @Test
    public void testC2ReportFlightAndReporter() {
        checkThatFieldIsAnnotatedWith(SafetyReport.class, "flight", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(SafetyReport.class, "flight", NotNull.class);
        checkThatFieldIsAnnotatedWith(SafetyReport.class, "reporter", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(SafetyReport.class, "reporter", NotNull.class);
    }

    @Test
    public void testC2ReportWitnesses() {
        checkThatFieldIsAnnotatedWith(SafetyReport.class, "witnesses", ManyToMany.class);
        SafetyReport r = TestC1.createValidReport(em, "SR-0903");
        em.persist(r);
        assertDoesNotThrow(() -> em.flush(), "A report without witnesses must be valid (0..3)");
        r.setWitnesses(new ArrayList<>(List.of(em.find(User.class, 5), em.find(User.class, 6), em.find(User.class, 7))));
        assertDoesNotThrow(() -> em.flush(), "A report with three witnesses must be valid");
        r.setWitnesses(new ArrayList<>(List.of(em.find(User.class, 5), em.find(User.class, 6), em.find(User.class, 7),
                em.find(User.class, 8))));
        assertThrows(Exception.class, () -> em.flush(), "A report cannot have more than three witnesses");
    }

    @Test
    public void testC2ActionBelongsToOneReport() {
        checkThatFieldIsAnnotatedWith(ReportAction.class, "report", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(ReportAction.class, "report", NotNull.class);
    }

    @Test
    public void testC2ReportActionsIsTheInverseSide() throws NoSuchFieldException {
        checkThatFieldIsAnnotatedWith(SafetyReport.class, "actions", OneToMany.class);
        OneToMany oneToMany = SafetyReport.class.getDeclaredField("actions").getAnnotation(OneToMany.class);
        assertEquals("report", oneToMany.mappedBy(),
                "'actions' must be the inverse side of the relation: @OneToMany(mappedBy = \"report\")");
        Integer joinTables = new JdbcTemplate(dataSource).queryForObject(
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(TABLE_NAME) = 'SAFETY_REPORT_ACTIONS'",
                Integer.class);
        assertEquals(0, joinTables, "The relation between reports and actions must not generate a join table");
    }

    @Test
    public void testC2BothSidesAreNavigable() {
        ReportAction a = TestC1.createValidAction(em);
        em.persist(a);
        em.flush();
        Integer reportId = a.getReport() == null ? null : (Integer) getFieldValueReflexively(a.getReport(), "id");
        em.clear();
        SafetyReport stored = em.find(SafetyReport.class, reportId);
        assertNotNull(stored.getActions(), "The actions of a report must be navigable from the report");
        assertEquals(1, stored.getActions().size());
    }

    @Test
    public void testC2AssigneeIsOptional() {
        checkThatFieldIsAnnotatedWith(ReportAction.class, "assignee", ManyToOne.class);
        ReportAction a = TestC1.createValidAction(em);
        a.setAssignee(null);
        assertDoesNotThrow(() -> {
            em.persist(a);
            em.flush();
        }, "An action without assignee must be valid (0..1)");
    }

}
