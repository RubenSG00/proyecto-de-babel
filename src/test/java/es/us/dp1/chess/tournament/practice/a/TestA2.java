package es.us.dp1.chess.tournament.practice.a;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@DataJpaTest()
public class TestA2 extends ReflexiveTest {

    @Autowired(required = false)
    EntityManager em;

    @Test
    public void testA2NoticeProgramAnnotation() {
        checkThatFieldIsAnnotatedWith(ProgramNotice.class, "program", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(ProgramNotice.class, "program", NotNull.class);
    }

    @Test
    public void testA2NoticeTargetsAnnotation() {
        checkThatFieldIsAnnotatedWith(ProgramNotice.class, "targets", ManyToMany.class);
    }

    @Test
    public void testA2NoticeTargetsConstraints() {
        ProgramNotice n = TestA1.createValidNotice(em);
        em.persist(n);

        checkThatFieldIsMandatory(n, "targets", List.class, em);

        n.setTargets(new ArrayList<>());
        assertThrows(Exception.class, () -> em.flush(),
                "You are not constraining 'targets' to a minimum of 1 element, since an empty list was considered valid");
    }

    @Test
    public void testA2AcknowledgementNoticeAnnotation() {
        checkThatFieldIsAnnotatedWith(NoticeAcknowledgement.class, "notice", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(NoticeAcknowledgement.class, "notice", NotNull.class);
    }

    @Test
    public void testA2AcknowledgementMemberAnnotation() {
        checkThatFieldIsAnnotatedWith(NoticeAcknowledgement.class, "member", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(NoticeAcknowledgement.class, "member", NotNull.class);
    }

    @Test
    public void testA2AcknowledgementDelegateIsOptional() throws Exception {
        checkThatFieldIsAnnotatedWith(NoticeAcknowledgement.class, "delegate", ManyToOne.class);
        // (ReflexiveTest.isFieldAnnotatedWith returns true when the annotation is absent, so it is not used here)
        assertFalse(NoticeAcknowledgement.class.getDeclaredField("delegate").isAnnotationPresent(NotNull.class),
                "'delegate' has cardinality 0..1, so it must not be annotated with @NotNull");

        NoticeAcknowledgement a = TestA1.createValidAcknowledgement(em);
        a.setDelegate(null);
        assertDoesNotThrow(() -> {
            em.persist(a);
            em.flush();
        }, "An acknowledgement without delegate must be valid, since the cardinality of 'delegate' is 0..1");

        a.setDelegate(em.find(User.class, 5));
        assertDoesNotThrow(() -> em.flush(), "An acknowledgement with a delegate must be valid");
        em.clear();
        NoticeAcknowledgement stored = em.find(NoticeAcknowledgement.class, getFieldValueReflexively(a, "id"));
        assertNotNull(stored.getDelegate(), "The delegate was not stored");
        assertEquals(5, stored.getDelegate().getId());
    }

}
