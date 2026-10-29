package es.us.dp1.chess.tournament.practice.x.transactions;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// No @Transactional on the tests: each service call commits or rolls back for real, so the
// grader can see whether the receipt of a failed enrollment stays in the database.
@SpringBootTest
public class TestX08 {

    @Autowired
    EnrollmentService service;

    @Autowired
    WorkshopRepository workshops;

    @Autowired
    ReceiptRepository receipts;

    @Autowired
    EnrollmentRepository enrollments;

    @AfterEach
    public void restoreInitialData() {
        enrollments.deleteAll();
        receipts.deleteAll();
        int[] initiallyEnrolled = { 1, 1, 0 };
        for (int id = 1; id <= 3; id++) {
            Workshop w = workshops.findById(id).get();
            w.setEnrolled(initiallyEnrolled[id - 1]);
            workshops.save(w);
        }
    }

    private int enrolledIn(int workshopId) {
        return workshops.findById(workshopId).get().getEnrolled();
    }

    @Test
    public void testX08EnrollInAWorkshopWithFreeSeats() throws WorkshopFullException {
        Enrollment e = service.enroll(3, "hikaru", 15);
        assertNotNull(e);
        assertEquals(1, enrolledIn(3));
        assertEquals(1, receipts.count());
        assertEquals(1, enrollments.count());
    }

    @Test
    public void testX08FullWorkshopLeavesNoReceipt() {
        long receiptsBefore = receipts.count();
        assertThrows(WorkshopFullException.class, () -> service.enroll(2, "fabiano", 15));
        assertEquals(receiptsBefore, receipts.count(),
                "The receipt of an enrollment in a full workshop must not remain in the database");
        assertEquals(1, enrolledIn(2));
        assertEquals(0, enrollments.count());
    }

    @Test
    public void testX08EnrollAllEnrollsEveryAttendee() throws WorkshopFullException {
        List<Enrollment> result = service.enrollAll(3, List.of("anish", "wesley", "levon"), 10);
        assertNotNull(result, "enrollAll must return the enrollments");
        assertEquals(3, result.size());
        assertEquals(3, enrolledIn(3));
        assertEquals(3, receipts.count());
        assertEquals(3, enrollments.count());
    }

    @Test
    public void testX08EnrollAllIsAtomic() {
        // Workshop 1 has one free seat: the first attendee fits, the second one does not
        assertThrows(WorkshopFullException.class, () -> service.enrollAll(1, List.of("ian", "ding"), 10));
        assertEquals(1, enrolledIn(1), "The enrollment of the first attendee must be undone");
        assertEquals(0, receipts.count(), "No receipt may remain, not even the one of the first attendee");
        assertEquals(0, enrollments.count(), "No enrollment may remain");
    }

}
