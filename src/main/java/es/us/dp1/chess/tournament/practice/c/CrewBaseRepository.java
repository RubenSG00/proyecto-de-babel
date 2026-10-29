package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CrewBaseRepository extends CrudRepository<CrewBase, Integer> {

    @Query("SELECT -1 FROM CrewBase b")
    List<Object[]> findPilotScores(Integer baseId, Integer minScore);
    // TODO: Implement to solve Test C6

}
