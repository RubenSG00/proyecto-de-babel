package es.us.dp1.chess.tournament.practice.a;

import static org.junit.jupiter.api.Assertions.*;

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
public class TestA1 extends ReflexiveTest {

    @Autowired(required = false)
    ProgramNoticeRepository programNoticeRepository;

    @Autowired(required = false)
    NoticeAcknowledgementRepository noticeAcknowledgementRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testA1RepositoriesExist() {
        assertNotNull(programNoticeRepository,
                "The ProgramNotice repository was not injected into the tests, its autowired value was null");
        assertNotNull(noticeAcknowledgementRepository,
                "The NoticeAcknowledgement repository was not injected into the tests, its autowired value was null");
        Object v = programNoticeRepository.findById(474629);
        assertFalse(null != v && ((Optional) v).isPresent(),
                "No result (null) should be returned for a notice that does not exist");
        v = noticeAcknowledgementRepository.findById(474629);
        assertFalse(null != v && ((Optional) v).isPresent(),
                "No result (null) should be returned for an acknowledgement that does not exist");
    }

    @Test
    public void testA1CheckNoticeConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "text", List.of(
                        "Gym",
                        "      ",
                        "This notice text is deliberately longer than the eighty characters that are allowed."),
                "readCount", List.of(-1));

        ProgramNotice n = createValidNotice(em);
        em.persist(n);
        checkThatEntityIsValid(n, em);

        n.setPublishedAt(null);
        checkThatEntityIsValid(n, em);

        checkThatFieldsAreMandatory(n, em, "text", "priority", "readCount");

        checkThatValuesAreNotValid(n, invalidValues, em);
    }

    @Test
    public void testA1CheckAcknowledgementConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "comment", List.of("x".repeat(151)),
                "acknowledgedAt", List.of(LocalDateTime.now().plusDays(1)));

        NoticeAcknowledgement a = createValidAcknowledgement(em);
        em.persist(a);
        checkThatEntityIsValid(a, em);

        a.setComment(null);
        checkThatEntityIsValid(a, em);
        a.setComment("x".repeat(150));
        checkThatEntityIsValid(a, em);

        checkThatFieldsAreMandatory(a, em, "status", "acknowledgedAt");

        checkThatValuesAreNotValid(a, invalidValues, em);
    }

    @Test
    public void testA1CheckNoticeAnnotations() throws NoSuchFieldException, SecurityException {
        assertTrue(classIsAnnotatedWith(ProgramNotice.class, Entity.class), "ProgramNotice is not an entity");
        checkEnumeratedAsString(ProgramNotice.class, "priority");
    }

    @Test
    public void testA1CheckAcknowledgementAnnotations() throws NoSuchFieldException, SecurityException {
        assertTrue(classIsAnnotatedWith(NoticeAcknowledgement.class, Entity.class),
                "NoticeAcknowledgement is not an entity");
        checkEnumeratedAsString(NoticeAcknowledgement.class, "status");
    }

    private void checkEnumeratedAsString(Class<?> type, String fieldName) throws NoSuchFieldException {
        java.lang.reflect.Field field = type.getDeclaredField(fieldName);
        assertTrue(field.isAnnotationPresent(Enumerated.class),
                "The attribute '" + fieldName + "' lacks the @Enumerated annotation");
        assertEquals(EnumType.STRING, field.getAnnotation(Enumerated.class).value(),
                "The @Enumerated annotation of '" + fieldName + "' is not configured with EnumType.STRING");
    }

    public static ProgramNotice createValidNotice(EntityManager em) {
        ProgramNotice n = new ProgramNotice();
        n.setText("Bring your own towel");
        n.setPriority(NoticePriority.NORMAL);
        n.setPublishedAt(LocalDateTime.of(2026, 9, 1, 8, 0));
        n.setReadCount(0);
        if (em != null) {
            n.setProgram(em.find(TrainingProgram.class, 1));
            n.setTargets(new ArrayList<>(List.of(em.find(User.class, 4))));
        }
        return n;
    }

    public static NoticeAcknowledgement createValidAcknowledgement(EntityManager em) {
        NoticeAcknowledgement a = new NoticeAcknowledgement();
        a.setComment("Thanks, noted");
        a.setStatus(AcknowledgementStatus.CONFIRMED);
        a.setAcknowledgedAt(LocalDateTime.of(2026, 9, 1, 9, 15));
        if (em != null) {
            ProgramNotice n = createValidNotice(em);
            em.persist(n);
            a.setNotice(n);
            a.setMember(em.find(User.class, 4));
        }
        return a;
    }

}
