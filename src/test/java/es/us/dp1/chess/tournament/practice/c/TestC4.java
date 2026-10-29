package es.us.dp1.chess.tournament.practice.c;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.user.User;
import jakarta.persistence.EntityManager;

@DataJpaTest
public class TestC4 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Test
    public void testC4Report1Witnesses() {
        checkContainsById(SafetyReport.class, 1, "getWitnesses", 5, em);
        checkContainsById(SafetyReport.class, 1, "getWitnesses", 6, em);
        assertThat(em.find(SafetyReport.class, 1).getWitnesses()).hasSize(2);
    }

    @Test
    public void testC4Report2Witnesses() {
        checkContainsById(SafetyReport.class, 2, "getWitnesses", 4, em);
        checkContainsById(SafetyReport.class, 2, "getWitnesses", 6, em);
        checkContainsById(SafetyReport.class, 2, "getWitnesses", 10, em);
        assertThat(em.find(SafetyReport.class, 2).getWitnesses()).hasSize(3);
    }

    @Test
    public void testC4Report3HasNoWitnesses() {
        List<User> witnesses = em.find(SafetyReport.class, 3).getWitnesses();
        assertThat(witnesses == null ? List.of() : witnesses).isEmpty();
        assertThat(em.find(SafetyReport.class, 1).getWitnesses()).isNotEmpty();
    }

    @Test
    public void testC4ActionsAreNavigableFromTheReports() {
        assertThat(em.find(SafetyReport.class, 1).getActions()).extracting(a -> getFieldValueReflexively(a, "id")).containsExactly(1);
        assertThat(em.find(SafetyReport.class, 2).getActions()).extracting(a -> getFieldValueReflexively(a, "id"))
                .containsExactlyInAnyOrder(2, 3);
        List<ReportAction> none = em.find(SafetyReport.class, 3).getActions();
        assertThat(none == null ? List.of() : none).isEmpty();
    }

}
