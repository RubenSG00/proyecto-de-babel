package es.us.dp1.chess.tournament.practice.a;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TrainingProgramRepository extends CrudRepository<TrainingProgram, Integer> {

    @Query("SELECT -1 FROM TrainingProgram p")
    List<Object[]> findMemberPoints(Integer programId);
    // TODO: Implement to solve Test A6

}
