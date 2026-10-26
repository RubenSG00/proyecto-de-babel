package es.us.dp1.chess.tournament.challenge;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FriendlyChallengeRepository extends CrudRepository<FriendlyChallenge, Integer> {

    @Query("SELECT -1 FROM FriendlyChallenge fc")
    List<Object[]> findParticipantPoints(Integer challengeId);
    // TODO: Implement to solve Test 6

}
