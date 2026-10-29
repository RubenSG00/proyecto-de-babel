package es.us.dp1.chess.tournament.practice.x.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@DataJpaTest
public class TestX05 {

    @Autowired
    MusicianRepository repo;

    static List<String> names(List<Musician> musicians) {
        return musicians.stream().map(Musician::getName).toList();
    }

    @Test
    public void testX05MusiciansPlayingUsesAPositionalParameter() throws NoSuchMethodException {
        Method m = MusicianRepository.class.getMethod("musiciansPlaying", String.class);
        assertTrue(m.getAnnotation(Query.class).value().contains("?1"),
                "musiciansPlaying must use the positional parameter ?1");
        assertThat(names(repo.musiciansPlaying("guitar"))).containsExactly("Ana Torres", "Carla Ruiz", "Fabio Moreno");
        assertThat(repo.musiciansPlaying("violin")).isEmpty();
    }

    @Test
    public void testX05MusiciansEarningBetweenUsesNamedParameters() throws NoSuchMethodException {
        Method m = MusicianRepository.class.getMethod("musiciansEarningBetween", Integer.class, Integer.class);
        String query = m.getAnnotation(Query.class).value();
        assertTrue(query.contains(":low") && query.contains(":high"),
                "musiciansEarningBetween must use the named parameters :low and :high");
        Param p0 = m.getParameters()[0].getAnnotation(Param.class);
        Param p1 = m.getParameters()[1].getAnnotation(Param.class);
        assertTrue(p0 != null && p1 != null, "Both parameters must be bound with @Param");
        assertEquals("low", p0.value());
        assertEquals("high", p1.value());
    }

    @Test
    public void testX05MusiciansEarningBetweenIncludesBothEndsSortedBySalaryDescending() {
        List<Musician> r = repo.musiciansEarningBetween(1500, 2500);
        assertThat(names(r)).containsExactlyInAnyOrder("Ana Belen", "Ana Torres", "Bruno Diaz", "Diego Lopez");
        assertThat(r.stream().map(Musician::getSalary).toList()).containsExactly(2500, 2000, 1500, 1500);
    }

    @Test
    public void testX05MusiciansNamedLike() {
        assertThat(names(repo.musiciansNamedLike("Ana"))).containsExactly("Ana Belen", "Ana Torres");
        assertThat(repo.musiciansNamedLike("ana")).as("the prefix is case-sensitive").isEmpty();
        assertThat(repo.musiciansNamedLike("Torres")).as("only names that start with the prefix").isEmpty();
    }

    @Test
    public void testX05MusiciansWithoutAgent() {
        assertThat(names(repo.musiciansWithoutAgent()))
                .containsExactly("Ana Belen", "Bruno Diaz", "Diego Lopez", "Fabio Moreno");
    }

}
