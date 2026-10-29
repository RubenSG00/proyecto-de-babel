package es.us.dp1.chess.tournament.practice.b;

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
public class TestB2 extends ReflexiveTest {

    @Autowired(required = false)
    EntityManager em;

    @Test
    public void testB2ReviewClubAnnotation() {
        checkThatFieldIsAnnotatedWith(BookReview.class, "club", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(BookReview.class, "club", NotNull.class);
    }

    @Test
    public void testB2ReviewAuthorAnnotation() {
        checkThatFieldIsAnnotatedWith(BookReview.class, "author", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(BookReview.class, "author", NotNull.class);
    }

    @Test
    public void testB2ReviewMentionedMembersAnnotation() {
        checkThatFieldIsAnnotatedWith(BookReview.class, "mentionedMembers", ManyToMany.class);
    }

    @Test
    public void testB2ReviewCanMentionNobody() {
        BookReview r = TestB1.createValidReview(em);
        r.setMentionedMembers(new ArrayList<>());
        assertDoesNotThrow(() -> {
            em.persist(r);
            em.flush();
        }, "A review that mentions nobody must be valid (cardinality 0..3)");
    }

    @Test
    public void testB2ReviewCanMentionThreeMembersButNotFour() {
        BookReview r = TestB1.createValidReview(em);
        em.persist(r);
        r.setMentionedMembers(new ArrayList<>(List.of(em.find(User.class, 5), em.find(User.class, 6),
                em.find(User.class, 7))));
        assertDoesNotThrow(() -> em.flush(), "A review that mentions three members must be valid");
        r.setMentionedMembers(new ArrayList<>(List.of(em.find(User.class, 5), em.find(User.class, 6),
                em.find(User.class, 7), em.find(User.class, 8))));
        assertThrows(Exception.class, () -> em.flush(),
                "You are not constraining 'mentionedMembers' to a maximum of 3 elements, since four were accepted");
    }

    @Test
    public void testB2ReactionReviewAnnotation() {
        checkThatFieldIsAnnotatedWith(ReviewReaction.class, "review", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(ReviewReaction.class, "review", NotNull.class);
    }

    @Test
    public void testB2ReactionReactorAnnotation() {
        checkThatFieldIsAnnotatedWith(ReviewReaction.class, "reactor", ManyToOne.class);
        checkThatFieldIsAnnotatedWith(ReviewReaction.class, "reactor", NotNull.class);
    }

}
