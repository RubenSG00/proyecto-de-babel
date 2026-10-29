package es.us.dp1.chess.tournament.practice.x.relationships;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.ReflexiveTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@DataJpaTest
public class TestX21 extends ReflexiveTest {

    @Autowired
    EntityManager em;

    @Autowired
    AthleteRepository athletes;

    @Autowired
    SportsLicenseRepository licenses;

    static Athlete athlete(String name, String license) {
        SportsLicense l = new SportsLicense();
        l.setNumber(license);
        l.setExpiresOn(LocalDate.of(2027, 12, 31));
        Athlete a = new Athlete();
        a.setName(name);
        a.setLicense(l);
        return a;
    }

    @Test
    public void testX21Mappings() {
        checkThatFieldIsAnnotatedWith(Team.class, "athletes", ManyToMany.class);
        checkThatFieldIsAnnotatedWith(Athlete.class, "license", OneToOne.class);
        checkThatFieldIsAnnotatedWith(Athlete.class, "license", NotNull.class);
    }

    @Test
    public void testX21SavingATeamSavesItsNewAthletesAndTheirLicenses() {
        Team t = new Team();
        t.setName("Sevilla Chess");
        t.setAthletes(new ArrayList<>(List.of(athlete("Ana", "L-1"), athlete("Luis", "L-2"))));
        em.persist(t);
        em.flush();
        assertEquals(2, athletes.count(), "Saving the team must save its new athletes");
        assertEquals(2, licenses.count(), "Saving an athlete must save its license");
    }

    @Test
    public void testX21DeletingATeamKeepsItsAthletes() {
        Team t = new Team();
        t.setName("Sevilla Chess");
        t.setAthletes(new ArrayList<>(List.of(athlete("Ana", "L-1"))));
        em.persist(t);
        em.flush();
        em.remove(t);
        em.flush();
        assertEquals(1, athletes.count(), "Deleting a team must not delete its athletes (aggregation)");
    }

    @Test
    public void testX21TheLicenseGoesWithItsAthlete() {
        Athlete a = athlete("Ana", "L-1");
        em.persist(a);
        em.flush();
        assertEquals(1, licenses.count());
        em.remove(a);
        em.flush();
        assertEquals(0, licenses.count(), "Deleting an athlete must delete its license");
    }

    @Test
    public void testX21AnAthleteNeedsALicense() {
        Athlete a = athlete("Ana", "L-1");
        a.setLicense(null);
        assertThrows(Exception.class, () -> {
            em.persist(a);
            em.flush();
        }, "An athlete without license must be rejected");
    }

}
