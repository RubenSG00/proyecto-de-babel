package es.us.dp1.chess.tournament.practice.a;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;

@DataJpaTest
public class TestA4 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Test
    public void testA4Notice1Targets() {
        checkContainsById(ProgramNotice.class, 1, "getTargets", 4, em);
        checkContainsById(ProgramNotice.class, 1, "getTargets", 5, em);
        assertEquals(2, em.find(ProgramNotice.class, 1).getTargets().size());
    }

    @Test
    public void testA4Notice2Targets() {
        checkContainsById(ProgramNotice.class, 2, "getTargets", 4, em);
        checkContainsById(ProgramNotice.class, 2, "getTargets", 5, em);
        checkContainsById(ProgramNotice.class, 2, "getTargets", 6, em);
        assertEquals(3, em.find(ProgramNotice.class, 2).getTargets().size());
    }

    @Test
    public void testA4Notice3Targets() {
        checkContainsById(ProgramNotice.class, 3, "getTargets", 5, em);
        checkContainsById(ProgramNotice.class, 3, "getTargets", 8, em);
        assertEquals(2, em.find(ProgramNotice.class, 3).getTargets().size());
    }

}
