package es.us.dp1.chess.tournament.practice.b;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.EntityManager;

@DataJpaTest
public class TestB4 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Test
    public void testB4Review1Mentions() {
        checkContainsById(BookReview.class, 1, "getMentionedMembers", 5, em);
        checkContainsById(BookReview.class, 1, "getMentionedMembers", 6, em);
        assertEquals(2, em.find(BookReview.class, 1).getMentionedMembers().size());
    }

    @Test
    public void testB4Review2Mentions() {
        checkContainsById(BookReview.class, 2, "getMentionedMembers", 4, em);
        assertEquals(1, em.find(BookReview.class, 2).getMentionedMembers().size());
    }

    @Test
    public void testB4Review3MentionsNobody() {
        List<User> mentioned = em.find(BookReview.class, 3).getMentionedMembers();
        assertTrue(mentioned == null || mentioned.isEmpty(), "The review with id:3 must not mention anybody");
        // The other reviews must have their mentions, so that an empty join table does not pass this test
        assertEquals(1, em.find(BookReview.class, 2).getMentionedMembers().size());
    }

}
