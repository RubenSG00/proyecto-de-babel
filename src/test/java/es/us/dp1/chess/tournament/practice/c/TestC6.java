package es.us.dp1.chess.tournament.practice.c;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.user.User;

@DataJpaTest
public class TestC6 {

    @Autowired
    CrewBaseRepository cbr;

    // Base 1, landed and not diverted: 1 (4 captain, 5 FO, long-haul), 2 (5, 6), 3 (4, 10),
    // 4 (13, 6, long-haul), 5 (10, 6). Flight 6 was diverted (7, 4) and flight 7 has not landed (13, 7).

    @Test
    void testC6ScoresOfBase1() {
        Map<Integer, Integer> scores = scoresAsMap(1, 0);
        assertThat(scores.get(4)).isEqualTo(7);
        assertThat(scores.get(5)).isEqualTo(5);
        assertThat(scores.get(6)).isEqualTo(4);
        assertThat(scores.get(10)).isEqualTo(4);
        assertThat(scores.get(13)).as("flight 7 has not landed: only flight 4 counts").isEqualTo(4);
        // User 7 only flew a diverted flight and a flight that has not landed
        assertThat(scores).hasSize(5);
    }

    @Test
    void testC6SortedByScoreThenUsername() {
        List<Object[]> result = cbr.findPilotScores(1, 0);
        // Tie at 4 points: "player10" < "player3" < "player7" as strings (id order would be 6, 10, 13)
        assertThat(result.stream().map(r -> ((User) r[0]).getUsername()).toList())
                .containsExactly("player1", "player2", "player10", "player3", "player7");
        assertThat(result.stream().map(r -> ((Number) r[1]).intValue()).toList()).containsExactly(7, 5, 4, 4, 4);
    }

    @Test
    void testC6MinScoreFiltersTheTotals() {
        assertThat(scoresAsMap(1, 5).keySet()).containsExactlyInAnyOrder(4, 5);
        assertThat(scoresAsMap(1, 4)).hasSize(5);
        assertThat(cbr.findPilotScores(1, 8)).isEmpty();
    }

    @Test
    void testC6BaseWithoutLandedNonDivertedFlights() {
        // The only flight of base 2 was diverted
        assertThat(cbr.findPilotScores(2, 0)).isEmpty();
    }

    private Map<Integer, Integer> scoresAsMap(Integer baseId, Integer minScore) {
        Map<Integer, Integer> scores = new HashMap<>();
        for (Object[] row : cbr.findPilotScores(baseId, minScore))
            scores.put(((User) row[0]).getId(), ((Number) row[1]).intValue());
        return scores;
    }

}
