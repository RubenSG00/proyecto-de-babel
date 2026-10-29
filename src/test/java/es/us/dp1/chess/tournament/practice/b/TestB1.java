package es.us.dp1.chess.tournament.practice.b;

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
public class TestB1 extends ReflexiveTest {

    @Autowired(required = false)
    BookReviewRepository bookReviewRepository;

    @Autowired(required = false)
    ReviewReactionRepository reviewReactionRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testB1RepositoriesExist() {
        assertNotNull(bookReviewRepository,
                "The BookReview repository was not injected into the tests, its autowired value was null");
        assertNotNull(reviewReactionRepository,
                "The ReviewReaction repository was not injected into the tests, its autowired value was null");
        Object v = bookReviewRepository.findById(474629);
        assertFalse(null != v && ((Optional) v).isPresent(),
                "No result (null) should be returned for a review that does not exist");
        v = reviewReactionRepository.findById(474629);
        assertFalse(null != v && ((Optional) v).isPresent(),
                "No result (null) should be returned for a reaction that does not exist");
    }

    @Test
    public void testB1CheckReviewConstraints() {
        Map<String, List<Object>> invalidValues = Map.of(
                "content", List.of("Too short review", "x".repeat(501)),
                "stars", List.of(0, 6),
                "publishedOn", List.of(LocalDate.now().plusDays(1)));

        BookReview r = createValidReview(em);
        em.persist(r);
        checkThatEntityIsValid(r, em);

        r.setPublishedOn(null);
        checkThatEntityIsValid(r, em);
        r.setStars(1);
        checkThatEntityIsValid(r, em);
        r.setStars(5);
        checkThatEntityIsValid(r, em);
        r.setContent("x".repeat(20));
        checkThatEntityIsValid(r, em);

        checkThatFieldsAreMandatory(r, em, "content", "stars", "visibility");

        checkThatValuesAreNotValid(r, invalidValues, em);
    }

    @Test
    public void testB1CheckReactionConstraints() {
        Map<String, List<Object>> invalidValues = Map.of("note", List.of("x".repeat(101)));

        ReviewReaction r = createValidReaction(em);
        em.persist(r);
        checkThatEntityIsValid(r, em);

        r.setNote(null);
        checkThatEntityIsValid(r, em);

        checkThatFieldsAreMandatory(r, em, "kind", "reactedAt");

        checkThatValuesAreNotValid(r, invalidValues, em);
    }

    @Test
    public void testB1CheckReviewAnnotations() throws NoSuchFieldException {
        assertTrue(classIsAnnotatedWith(BookReview.class, Entity.class), "BookReview is not an entity");
        checkEnumeratedAsString(BookReview.class, "visibility");
    }

    @Test
    public void testB1CheckReactionAnnotations() throws NoSuchFieldException {
        assertTrue(classIsAnnotatedWith(ReviewReaction.class, Entity.class), "ReviewReaction is not an entity");
        checkEnumeratedAsString(ReviewReaction.class, "kind");
    }

    private void checkEnumeratedAsString(Class<?> type, String fieldName) throws NoSuchFieldException {
        java.lang.reflect.Field field = type.getDeclaredField(fieldName);
        assertTrue(field.isAnnotationPresent(Enumerated.class),
                "The attribute '" + fieldName + "' lacks the @Enumerated annotation");
        assertEquals(EnumType.STRING, field.getAnnotation(Enumerated.class).value(),
                "The @Enumerated annotation of '" + fieldName + "' is not configured with EnumType.STRING");
    }

    public static BookReview createValidReview(EntityManager em) {
        BookReview r = new BookReview();
        r.setContent("A gripping story about memory and loss");
        r.setStars(4);
        r.setVisibility(ReviewVisibility.PUBLIC);
        r.setPublishedOn(LocalDate.of(2026, 9, 10));
        if (em != null) {
            r.setClub(em.find(ReadingClub.class, 1));
            r.setAuthor(em.find(User.class, 4));
            r.setMentionedMembers(new ArrayList<>());
        }
        return r;
    }

    public static ReviewReaction createValidReaction(EntityManager em) {
        ReviewReaction r = new ReviewReaction();
        r.setKind(ReactionKind.LIKE);
        r.setReactedAt(LocalDateTime.of(2026, 9, 10, 20, 0));
        r.setNote("Agreed");
        if (em != null) {
            BookReview review = createValidReview(em);
            em.persist(review);
            r.setReview(review);
            r.setReactor(em.find(User.class, 5));
        }
        return r;
    }

}
