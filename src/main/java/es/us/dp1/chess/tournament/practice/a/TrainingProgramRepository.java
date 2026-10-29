package es.us.dp1.chess.tournament.practice.a;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TrainingProgramRepository extends CrudRepository<TrainingProgram, Integer> {

    // "Took part in a finished session" is a row filter (WHERE); the points of each session
    // are decided in the CASE, so members whose sessions failed still appear with 0.
    @Query("SELECT u, SUM(CASE WHEN s.trainee = u AND s.goalAchieved = true THEN 3 "
            + "WHEN s.coach = u AND s.goalAchieved = true THEN 1 ELSE 0 END) "
            + "FROM TrainingProgram p JOIN p.members u JOIN p.sessions s "
            + "WHERE p.id = :programId AND s.finish IS NOT NULL AND (s.trainee = u OR s.coach = u) "
            + "GROUP BY u ORDER BY 2 DESC")
    List<Object[]> findMemberPoints(@Param("programId") Integer programId);

}
