package es.us.dp1.chess.tournament.challenge;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FriendlyChallengeRepository extends CrudRepository<FriendlyChallenge, Integer> {

    // One row per participant who played at least one finished match of the challenge:
    // the "finish IS NOT NULL" filter goes in the WHERE (not in the CASE) so that unfinished
    // matches do not even make a player appear; "m.white = u OR m.black = u" keeps only the
    // matches the participant played, so a draw only gives points to its two players.
    @Query("SELECT u, SUM(CASE WHEN m.winner = u THEN 2 WHEN m.winner IS NULL THEN 1 ELSE 0 END) "
            + "FROM FriendlyChallenge fc JOIN fc.participants u JOIN fc.matches m "
            + "WHERE fc.id = :challengeId AND m.finish IS NOT NULL AND (m.white = u OR m.black = u) "
            + "GROUP BY u ORDER BY 2 DESC")
    List<Object[]> findParticipantPoints(@Param("challengeId") Integer challengeId);

}
