package es.us.dp1.chess.tournament.practice.x.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;

@DataJpaTest
public class TestX03 {

    @Autowired
    ConcertRepository repo;

    // The methods are invoked by reflection so that this grader compiles before they are declared
    static Object call(Object repo, String name, Class<?>[] types, Object... args) {
        Method m;
        try {
            m = ConcertRepository.class.getMethod(name, types);
        } catch (NoSuchMethodException e) {
            fail("ConcertRepository does not declare the method " + name);
            return null;
        }
        assertNull(m.getAnnotation(Query.class), name + " must be a derived query, without @Query");
        try {
            return m.invoke(repo, args);
        } catch (InvocationTargetException e) {
            fail("The method " + name + " threw " + e.getTargetException());
        } catch (IllegalAccessException e) {
            fail("The method " + name + " is not accessible");
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static List<Integer> ids(Object result) {
        return ((List<Concert>) result).stream().map(Concert::getId).toList();
    }

    @Test
    public void testX03FindByCity() {
        Object r = call(repo, "findByCity", new Class[] { String.class }, "Seville");
        assertThat(ids(r)).containsExactlyInAnyOrder(1, 3, 6, 8);
        assertThat(ids(call(repo, "findByCity", new Class[] { String.class }, "Paris"))).isEmpty();
    }

    @Test
    public void testX03FindByPriceLessThanEqualOrderByPriceAsc() {
        Object r = call(repo, "findByPriceLessThanEqualOrderByPriceAsc", new Class[] { Integer.class }, 40);
        assertThat(ids(r)).containsExactly(7, 3, 1, 6);
    }

    @Test
    public void testX03FindByDateBetween() {
        Object r = call(repo, "findByDateBetween", new Class[] { LocalDate.class, LocalDate.class },
                LocalDate.of(2026, 11, 5), LocalDate.of(2026, 12, 1));
        assertThat(ids(r)).containsExactlyInAnyOrder(1, 2, 3, 8);
    }

    @Test
    public void testX03FindBySoldOutFalse() {
        Object r = call(repo, "findBySoldOutFalse", new Class[] {});
        assertThat(ids(r)).containsExactlyInAnyOrder(1, 3, 4, 6, 7);
    }

    @Test
    public void testX03FindByTitleContainingIgnoreCase() {
        Object r = call(repo, "findByTitleContainingIgnoreCase", new Class[] { String.class }, "ROCK");
        assertThat(ids(r)).containsExactlyInAnyOrder(1, 5, 8);
    }

    @Test
    public void testX03FindByGenreIn() {
        Object r = call(repo, "findByGenreIn", new Class[] { Collection.class },
                List.of(MusicGenre.JAZZ, MusicGenre.CLASSICAL));
        assertThat(ids(r)).containsExactlyInAnyOrder(2, 4, 6, 8);
    }

    @Test
    public void testX03FindByTitle() {
        Object found = call(repo, "findByTitle", new Class[] { String.class }, "Pop Parade");
        assertTrue(found instanceof Optional, "findByTitle must return an Optional");
        assertEquals(3, ((Optional<Concert>) found).get().getId());
        Object missing = call(repo, "findByTitle", new Class[] { String.class }, "Pop");
        assertTrue(((Optional<Concert>) missing).isEmpty(), "findByTitle must look for the exact title");
    }

}
