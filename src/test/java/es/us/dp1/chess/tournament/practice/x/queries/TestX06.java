package es.us.dp1.chess.tournament.practice.x.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;

@DataJpaTest
public class TestX06 {

    @Autowired
    MusicianRepository repo;

    @Test
    public void testX06MusiciansWithoutBand() {
        assertThat(TestX05.names(repo.musiciansWithoutBand())).containsExactlyInAnyOrder("Ana Belen", "Fabio Moreno");
    }

    @Test
    public void testX06MusiciansWithBand() {
        assertThat(TestX05.names(repo.musiciansWithBand()))
                .containsExactlyInAnyOrder("Ana Torres", "Bruno Diaz", "Carla Ruiz", "Diego Lopez", "Elena Gil");
    }

    @Test
    public void testX06MusiciansPlayingInCityAppearOnlyOnce() {
        // Carla Ruiz plays in two bands of Seville: without DISTINCT she would appear twice
        assertThat(TestX05.names(repo.musiciansPlayingInCity("Seville")))
                .containsExactlyInAnyOrder("Ana Torres", "Bruno Diaz", "Carla Ruiz", "Elena Gil");
        assertThat(TestX05.names(repo.musiciansPlayingInCity("Madrid")))
                .containsExactlyInAnyOrder("Bruno Diaz", "Diego Lopez");
        assertThat(repo.musiciansPlayingInCity("Valencia")).isEmpty();
    }

    @Test
    public void testX06MusiciansEarningMoreThan() {
        assertThat(TestX05.names(repo.musiciansEarningMoreThan("Ana Torres")))
                .containsExactlyInAnyOrder("Carla Ruiz", "Elena Gil", "Ana Belen");
        assertThat(repo.musiciansEarningMoreThan("Elena Gil")).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testX06InstrumentsPlayedInCityWithoutRepetitions() throws Exception {
        // Invoked by reflection, so that this grader compiles before the method is declared
        Method m;
        try {
            m = MusicianRepository.class.getMethod("instrumentsPlayedInCity", String.class);
        } catch (NoSuchMethodException e) {
            fail("MusicianRepository does not declare the method instrumentsPlayedInCity(String)");
            return;
        }
        assertNotNull(m.getAnnotation(Query.class), "instrumentsPlayedInCity must be annotated with @Query");
        // Without DISTINCT, Seville would give drums, guitar, guitar, guitar, piano
        assertThat((List<String>) m.invoke(repo, "Seville")).containsExactly("drums", "guitar", "piano");
        assertThat((List<String>) m.invoke(repo, "Madrid")).containsExactly("bass", "drums");
        assertThat((List<String>) m.invoke(repo, "Valencia")).isEmpty();
    }

    @Test
    public void testX06CountMusiciansOfBand() {
        assertEquals(3L, repo.countMusiciansOfBand(1));
        assertEquals(2L, repo.countMusiciansOfBand(2));
        assertEquals(2L, repo.countMusiciansOfBand(3));
        assertEquals(0L, repo.countMusiciansOfBand(4));
    }

}
