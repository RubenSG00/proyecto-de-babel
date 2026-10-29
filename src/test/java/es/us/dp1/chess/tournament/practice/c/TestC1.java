package es.us.dp1.chess.tournament.practice.c;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@DataJpaTest()
public class TestC1 extends ReflexiveTest {

    @Autowired(required = false)
    SafetyReportRepository safetyReportRepository;

    @Autowired(required = false)
    ReportActionRepository reportActionRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testC1RepositoriesExist() {
        assertNotNull(safetyReportRepository,
                "The SafetyReport repository was not injected into the tests, its autowired value was null");
        assertNotNull(reportActionRepository,
                "The ReportAction repository was not injected into the tests, its autowired value was null");
        Object v = safetyReportRepository.findById(474629);
        assertFalse(null != v && ((Optional) v).isPresent(),
                "No result (null) should be returned for a report that does not exist");
        v = reportActionRepository.findById(474629);
        assertFalse(null != v && ((Optional) v).isPresent(),
                "No result (null) should be returned for an action that does not exist");
    }

    @Test
    public void testC1CheckReportConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "code", List.of("SR-42", "SR-00420", "sr-0042", "XX-0042", "SR0042", "SR-004a"),
                "title", List.of("Bird", "       ", "x".repeat(61)),
                "description", List.of("x".repeat(501)),
                "reportedAt", List.of(LocalDateTime.now().plusHours(1)),
                "delayMinutes", List.of(-1, 1441));

        SafetyReport r = createValidReport(em, "SR-0901");
        em.persist(r);
        checkThatEntityIsValid(r, em);

        r.setDescription(null);
        r.setDelayMinutes(null);
        checkThatEntityIsValid(r, em);
        r.setDelayMinutes(0);
        checkThatEntityIsValid(r, em);
        r.setDelayMinutes(1440);
        checkThatEntityIsValid(r, em);

        checkThatFieldsAreMandatory(r, em, "code", "title", "severity", "reportedAt");

        checkThatValuesAreNotValid(r, invalidValues, em);
    }

    @Test
    public void testC1CodeIsUnique() {
        SafetyReport first = createValidReport(em, "SR-0777");
        em.persist(first);
        em.flush();
        SafetyReport second = createValidReport(em, "SR-0777");
        assertThrows(Exception.class, () -> {
            em.persist(second);
            em.flush();
        }, "Two reports with the same code were stored: the code must be unique in the database");
    }

    @Test
    public void testC1CheckActionConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "actionTaken", List.of("Too short", "x".repeat(201)),
                "priority", List.of(0, 4));

        ReportAction a = createValidAction(em);
        em.persist(a);
        checkThatEntityIsValid(a, em);

        a.setClosedAt(null);
        checkThatEntityIsValid(a, em);

        checkThatFieldsAreMandatory(a, em, "actionTaken", "status", "deadline", "priority");

        checkThatValuesAreNotValid(a, invalidValues, em);
    }

    @Test
    public void testC1CheckEnumerations() throws NoSuchFieldException {
        assertTrue(classIsAnnotatedWith(SafetyReport.class, Entity.class), "SafetyReport is not an entity");
        assertTrue(classIsAnnotatedWith(ReportAction.class, Entity.class), "ReportAction is not an entity");
        for (Object[] f : new Object[][] { { SafetyReport.class, "severity" }, { ReportAction.class, "status" } }) {
            java.lang.reflect.Field field = ((Class<?>) f[0]).getDeclaredField((String) f[1]);
            assertTrue(field.isAnnotationPresent(Enumerated.class), "'" + f[1] + "' lacks @Enumerated");
            assertEquals(EnumType.STRING, field.getAnnotation(Enumerated.class).value(),
                    "'" + f[1] + "' must be stored as a string");
        }
    }

    public static SafetyReport createValidReport(EntityManager em, String code) {
        SafetyReport r = new SafetyReport();
        r.setCode(code);
        r.setTitle("Bird strike on approach");
        r.setDescription("Minor dent on the left wing");
        r.setSeverity(Severity.MAJOR);
        r.setReportedAt(LocalDateTime.of(2026, 7, 2, 14, 20));
        r.setDelayMinutes(45);
        if (em != null) {
            r.setFlight(em.find(Flight.class, 1));
            r.setReporter(em.find(User.class, 4));
            r.setWitnesses(new ArrayList<>());
        }
        return r;
    }

    public static ReportAction createValidAction(EntityManager em) {
        ReportAction a = new ReportAction();
        a.setActionTaken("Full inspection of the left wing");
        a.setStatus(ActionStatus.CLOSED);
        a.setDeadline(LocalDate.of(2026, 7, 10));
        a.setClosedAt(LocalDateTime.of(2026, 7, 9, 12, 0));
        a.setPriority(1);
        if (em != null) {
            SafetyReport r = createValidReport(em, "SR-0902");
            em.persist(r);
            a.setReport(r);
        }
        return a;
    }

}
