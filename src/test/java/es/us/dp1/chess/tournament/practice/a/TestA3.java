package es.us.dp1.chess.tournament.practice.a;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;

@DataJpaTest()
public class TestA3 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Test
    public void testA3InitialNotice1() {
        ProgramNotice n = em.find(ProgramNotice.class, 1);
        assertNotNull(n, "There should exist a ProgramNotice with id:1");
        assertEquals("Bring your own towel from Monday", getFieldValueReflexively(n, "text"));
        assertEquals(NoticePriority.NORMAL, n.getPriority());
        assertEquals(LocalDateTime.of(2026, 9, 1, 8, 0), n.getPublishedAt());
        assertEquals(12, n.getReadCount());
        checkLinkedById(ProgramNotice.class, 1, "getProgram", 1, em);
    }

    @Test
    public void testA3InitialNotice2() {
        ProgramNotice n = em.find(ProgramNotice.class, 2);
        assertNotNull(n, "There should exist a ProgramNotice with id:2");
        assertEquals("Leg day moved to Thursday", getFieldValueReflexively(n, "text"));
        assertEquals(NoticePriority.URGENT, n.getPriority());
        assertEquals(LocalDateTime.of(2026, 9, 3, 18, 30), n.getPublishedAt());
        assertEquals(30, n.getReadCount());
        checkLinkedById(ProgramNotice.class, 2, "getProgram", 1, em);
    }

    @Test
    public void testA3InitialNotice3() {
        ProgramNotice n = em.find(ProgramNotice.class, 3);
        assertNotNull(n, "There should exist a ProgramNotice with id:3");
        assertEquals("Draft: new stretching routine", getFieldValueReflexively(n, "text"));
        assertEquals(NoticePriority.LOW, n.getPriority());
        assertNull(n.getPublishedAt());
        assertEquals(0, n.getReadCount());
        checkLinkedById(ProgramNotice.class, 3, "getProgram", 2, em);
    }

    @Test
    public void testA3InitialAcknowledgement1() {
        NoticeAcknowledgement a = em.find(NoticeAcknowledgement.class, 1);
        assertNotNull(a, "There should exist a NoticeAcknowledgement with id:1");
        assertEquals("Thanks, noted", getFieldValueReflexively(a, "comment"));
        assertEquals(AcknowledgementStatus.CONFIRMED, a.getStatus());
        assertEquals(LocalDateTime.of(2026, 9, 1, 9, 15), a.getAcknowledgedAt());
        checkLinkedById(NoticeAcknowledgement.class, 1, "getNotice", 1, em);
        checkLinkedById(NoticeAcknowledgement.class, 1, "getMember", 4, em);
        assertNull(a.getDelegate(), "The acknowledgement with id:1 has no delegate");
    }

    @Test
    public void testA3InitialAcknowledgement2() {
        NoticeAcknowledgement a = em.find(NoticeAcknowledgement.class, 2);
        assertNotNull(a, "There should exist a NoticeAcknowledgement with id:2");
        assertNull(a.getComment(), "The acknowledgement with id:2 has no comment");
        assertEquals(AcknowledgementStatus.SEEN, a.getStatus());
        assertEquals(LocalDateTime.of(2026, 9, 3, 19, 0), a.getAcknowledgedAt());
        checkLinkedById(NoticeAcknowledgement.class, 2, "getNotice", 2, em);
        checkLinkedById(NoticeAcknowledgement.class, 2, "getMember", 5, em);
        assertNull(a.getDelegate(), "The acknowledgement with id:2 has no delegate");
    }

    @Test
    public void testA3InitialAcknowledgement3() {
        NoticeAcknowledgement a = em.find(NoticeAcknowledgement.class, 3);
        assertNotNull(a, "There should exist a NoticeAcknowledgement with id:3");
        assertEquals("I can't make it on Thursday", getFieldValueReflexively(a, "comment"));
        assertEquals(AcknowledgementStatus.REJECTED, a.getStatus());
        assertEquals(LocalDateTime.of(2026, 9, 3, 20, 45), a.getAcknowledgedAt());
        checkLinkedById(NoticeAcknowledgement.class, 3, "getNotice", 2, em);
        checkLinkedById(NoticeAcknowledgement.class, 3, "getMember", 6, em);
        checkLinkedById(NoticeAcknowledgement.class, 3, "getDelegate", 4, em);
    }

}
