package es.us.dp1.chess.tournament.practice.x.queries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
    public void testX06CountMusiciansOfBand() {
        assertEquals(3L, repo.countMusiciansOfBand(1));
        assertEquals(2L, repo.countMusiciansOfBand(2));
        assertEquals(2L, repo.countMusiciansOfBand(3));
        assertEquals(0L, repo.countMusiciansOfBand(4));
    }

}
