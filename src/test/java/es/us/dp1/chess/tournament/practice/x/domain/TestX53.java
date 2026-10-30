package es.us.dp1.chess.tournament.practice.x.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class TestX53 {

    static Locker locker(Integer id, String code) {
        Locker l = new Locker();
        l.setId(id);
        l.setCode(code);
        l.setBuilding("Main");
        return l;
    }

    @Test
    public void testX53DifferentIdsAreDifferentLockers() {
        assertNotEquals(locker(1, "A1"), locker(2, "A1"), "Two lockers with different ids must not be equal");
    }

    @Test
    public void testX53TheSameIdIsTheSameLocker() {
        Locker a = locker(7, "A1");
        Locker b = locker(7, "B2");
        assertEquals(a, b, "Two lockers with the same id are equal, whatever their other attributes");
        assertEquals(a.hashCode(), b.hashCode(), "Equal objects must have the same hashCode");
        Set<Locker> set = new HashSet<>();
        set.add(a);
        set.add(b);
        assertEquals(1, set.size());
    }

    @Test
    public void testX53EdgeCases() {
        Locker a = locker(3, "A1");
        assertEquals(a, a);
        assertNotEquals(a, null);
        assertNotEquals(a, "A1");
        assertEquals(locker(null, "A1"), locker(null, "B2"), "Two lockers without id are equal, as in T4 p.47");
    }

}
