package es.us.dp1.chess.tournament.practice.a;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import es.us.dp1.chess.tournament.user.User;

@DataJpaTest
public class TestA6 {

    @Autowired
    TrainingProgramRepository tpr;

    @Test
    void testA6MemberPointsForProgram1() {
        // Program 1: session 1 (6 coaches 4, achieved), 2 (4 coaches 5, achieved),
        // 3 (5 coaches 6, not achieved) and 4 (7 coaches 4, achieved but unfinished)
        Map<Integer, Integer> points = memberPointsAsMap(1);
        assertThat(points.get(4)).isEqualTo(4);
        assertThat(points.get(5)).isEqualTo(3);
        assertThat(points.get(6)).isEqualTo(1);
        // User 7 only coached the unfinished session 4: it must not appear
        assertThat(points).hasSize(3);
    }

    @Test
    void testA6MemberPointsForProgramWithoutFinishedSessions() {
        assertThat(tpr.findMemberPoints(2)).isEmpty();
    }

    @Test
    void testA6MemberPointsForProgram3IncludesMembersWithZeroPoints() {
        // Program 3: session 6 (10 coaches 11, achieved), 7 (11 coaches 9, not achieved) and
        // 8 (9 coaches 4, unfinished with goalAchieved filled in advance)
        Map<Integer, Integer> points = memberPointsAsMap(3);
        assertThat(points.get(11)).isEqualTo(3);
        assertThat(points.get(10)).isEqualTo(1);
        assertThat(points.get(9)).isEqualTo(0);
        assertThat(points).hasSize(3);
    }

    @Test
    void testA6UnfinishedSessionsDoNotCount() {
        int total = memberPointsAsMap(1).values().stream().mapToInt(Integer::intValue).sum();
        // Only sessions 1 (3+1) and 2 (3+1) give points; session 4 would add 4 more
        assertThat(total).isEqualTo(8);
    }

    @Test
    void testA6MemberPointsAreSortedByPointsDescending() {
        List<Object[]> result = tpr.findMemberPoints(3);
        assertThat(result).hasSize(3);
        assertThat(((User) result.get(0)[0]).getId()).isEqualTo(11);
        assertThat(((Number) result.get(0)[1]).intValue()).isEqualTo(3);
        assertThat(((User) result.get(1)[0]).getId()).isEqualTo(10);
        assertThat(((Number) result.get(1)[1]).intValue()).isEqualTo(1);
        assertThat(((User) result.get(2)[0]).getId()).isEqualTo(9);
        assertThat(((Number) result.get(2)[1]).intValue()).isEqualTo(0);
    }

    private Map<Integer, Integer> memberPointsAsMap(Integer programId) {
        Map<Integer, Integer> points = new HashMap<>();
        for (Object[] row : tpr.findMemberPoints(programId))
            points.put(((User) row[0]).getId(), ((Number) row[1]).intValue());
        return points;
    }

}
