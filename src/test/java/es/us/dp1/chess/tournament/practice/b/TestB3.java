package es.us.dp1.chess.tournament.practice.b;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;

@DataJpaTest()
public class TestB3 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    private void checkReview(int id, String content, int stars, ReviewVisibility visibility, LocalDate publishedOn,
            int club, int author) {
        BookReview r = em.find(BookReview.class, id);
        assertNotNull(r, "There should exist a BookReview with id:" + id);
        assertEquals(content, getFieldValueReflexively(r, "content"));
        assertEquals(stars, r.getStars());
        assertEquals(visibility, r.getVisibility());
        assertEquals(publishedOn, r.getPublishedOn());
        checkLinkedById(BookReview.class, id, "getClub", club, em);
        checkLinkedById(BookReview.class, id, "getAuthor", author, em);
    }

    private void checkReaction(int id, ReactionKind kind, LocalDateTime reactedAt, String note, int review,
            int reactor) {
        ReviewReaction r = em.find(ReviewReaction.class, id);
        assertNotNull(r, "There should exist a ReviewReaction with id:" + id);
        assertEquals(kind, r.getKind());
        assertEquals(reactedAt, r.getReactedAt());
        assertEquals(note, r.getNote());
        checkLinkedById(ReviewReaction.class, id, "getReview", review, em);
        checkLinkedById(ReviewReaction.class, id, "getReactor", reactor, em);
    }

    @Test
    public void testB3InitialReview1() {
        checkReview(1, "A gripping story about memory and loss", 5, ReviewVisibility.PUBLIC, LocalDate.of(2026, 9, 10), 1, 4);
    }

    @Test
    public void testB3InitialReview2() {
        checkReview(2, "Too slow in the middle chapters, sadly", 2, ReviewVisibility.MEMBERS_ONLY, LocalDate.of(2026, 9, 12), 1, 5);
    }

    @Test
    public void testB3InitialReview3() {
        checkReview(3, "Personal notes, will finish them later", 3, ReviewVisibility.PRIVATE, null, 2, 8);
    }

    @Test
    public void testB3InitialReaction1() {
        checkReaction(1, ReactionKind.LIKE, LocalDateTime.of(2026, 9, 10, 20, 0), null, 1, 5);
    }

    @Test
    public void testB3InitialReaction2() {
        checkReaction(2, ReactionKind.LOVE, LocalDateTime.of(2026, 9, 11, 8, 30), "It's my favourite book!", 1, 6);
    }

    @Test
    public void testB3InitialReaction3() {
        checkReaction(3, ReactionKind.DISLIKE, LocalDateTime.of(2026, 9, 12, 22, 15), null, 2, 4);
    }

    @Test
    public void testB3InitialReaction4() {
        checkReaction(4, ReactionKind.LIKE, LocalDateTime.of(2026, 9, 13, 10, 0), "Agreed", 2, 7);
    }

}
