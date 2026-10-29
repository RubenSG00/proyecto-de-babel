package es.us.dp1.chess.tournament.practice.b;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.user.User;

@DataJpaTest
public class TestB6 {

    @Autowired
    ReadingClubRepository rcr;

    private static final LocalDate OCT_1 = LocalDate.of(2026, 10, 1);
    private static final LocalDate OCT_10 = LocalDate.of(2026, 10, 10);

    // Club 1: loan 1 (user 4, returned late), 2 (user 4, due 2026-10-05, not returned),
    // 3 (user 5, due 2026-09-25, not returned), 4 (user 6, returned on its due date),
    // 5 (user 9, not a member) and 6 (user 13, due 2026-09-28, not returned)

    @Test
    void testB6OverdueCountsAtOctober1() {
        Map<Integer, Integer> counts = countsAsMap(1, OCT_1);
        assertThat(counts.get(4)).isEqualTo(1);
        assertThat(counts.get(5)).isEqualTo(1);
        assertThat(counts.get(13)).isEqualTo(1);
        assertThat(counts.get(6)).as("returned exactly on its due date is not late").isEqualTo(0);
        // User 7 is a member without loans; user 9 borrowed a loan of the club but is not a member
        assertThat(counts).hasSize(4);
    }

    @Test
    void testB6TheReferenceDateIsUsedForLoansNotReturned() {
        Map<Integer, Integer> counts = countsAsMap(1, OCT_10);
        assertThat(counts.get(4)).isEqualTo(2);
        assertThat(countsAsMap(1, LocalDate.of(2026, 9, 1)).values()).containsOnly(1, 0);
    }

    @Test
    void testB6ClubWithoutLoans() {
        assertThat(rcr.findOverdueCounts(2, OCT_1)).isEmpty();
    }

    @Test
    void testB6SortedByOverdueLoansDescendingThenUsername() {
        List<Object[]> result = rcr.findOverdueCounts(1, OCT_1);
        // Tie at 1: usernames compared as strings, so "player10" goes before "player2"
        assertThat(usernames(result)).containsExactly("player1", "player10", "player2", "player3");
        List<Object[]> later = rcr.findOverdueCounts(1, OCT_10);
        assertThat(usernames(later)).containsExactly("player1", "player10", "player2", "player3");
        assertThat(((Number) later.get(0)[1]).intValue()).isEqualTo(2);
    }

    private static List<String> usernames(List<Object[]> rows) {
        return rows.stream().map(r -> ((User) r[0]).getUsername()).toList();
    }

    private Map<Integer, Integer> countsAsMap(Integer clubId, LocalDate date) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Object[] row : rcr.findOverdueCounts(clubId, date))
            counts.put(((User) row[0]).getId(), ((Number) row[1]).intValue());
        return counts;
    }

}
