package es.us.dp1.chess.tournament.practice.c;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;

@DataJpaTest()
public class TestC3 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    private void checkReport(int id, String code, String title, String description, Severity severity,
            LocalDateTime reportedAt, Integer delay, int flight, int reporter) {
        SafetyReport r = em.find(SafetyReport.class, id);
        assertNotNull(r, "There should exist a SafetyReport with id:" + id);
        assertEquals(code, r.getCode());
        assertEquals(title, getFieldValueReflexively(r, "title"));
        assertEquals(description, r.getDescription());
        assertEquals(severity, r.getSeverity());
        assertEquals(reportedAt, r.getReportedAt());
        assertEquals(delay, r.getDelayMinutes());
        checkLinkedById(SafetyReport.class, id, "getFlight", flight, em);
        checkLinkedById(SafetyReport.class, id, "getReporter", reporter, em);
    }

    private void checkAction(int id, String actionTaken, ActionStatus status, LocalDate deadline,
            LocalDateTime closedAt, int priority, int report, Integer assignee) {
        ReportAction a = em.find(ReportAction.class, id);
        assertNotNull(a, "There should exist a ReportAction with id:" + id);
        assertEquals(actionTaken, getFieldValueReflexively(a, "actionTaken"));
        assertEquals(status, a.getStatus());
        assertEquals(deadline, a.getDeadline());
        assertEquals(closedAt, a.getClosedAt());
        assertEquals(priority, a.getPriority());
        checkLinkedById(ReportAction.class, id, "getReport", report, em);
        if (assignee == null)
            assertNull(a.getAssignee(), "The action with id:" + id + " has no assignee");
        else
            checkLinkedById(ReportAction.class, id, "getAssignee", assignee, em);
    }

    @Test
    public void testC3InitialReport1() {
        checkReport(1, "SR-0001", "Bird strike on approach", "Minor dent on the left wing, inspected on arrival",
                Severity.MAJOR, LocalDateTime.of(2026, 7, 2, 14, 20), 45, 1, 4);
    }

    @Test
    public void testC3InitialReport2() {
        checkReport(2, "SR-0002", "Cabin pressure warning", null, Severity.CRITICAL,
                LocalDateTime.of(2026, 7, 5, 9, 10), null, 2, 5);
    }

    @Test
    public void testC3InitialReport3() {
        checkReport(3, "SR-0003", "Coffee machine out of order", null, Severity.MINOR,
                LocalDateTime.of(2026, 7, 6, 18, 0), 0, 3, 6);
    }

    @Test
    public void testC3InitialAction1() {
        checkAction(1, "Full inspection of the left wing", ActionStatus.CLOSED, LocalDate.of(2026, 7, 10),
                LocalDateTime.of(2026, 7, 9, 12, 0), 1, 1, 10);
    }

    @Test
    public void testC3InitialAction2() {
        checkAction(2, "Replace the pressure sensor of the cabin", ActionStatus.IN_PROGRESS, LocalDate.of(2026, 8, 1),
                null, 1, 2, 13);
    }

    @Test
    public void testC3InitialAction3() {
        checkAction(3, "Review the maintenance log of the aircraft", ActionStatus.OPEN, LocalDate.of(2026, 8, 15),
                null, 2, 2, null);
    }

}
