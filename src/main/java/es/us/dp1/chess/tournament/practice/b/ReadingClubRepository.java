package es.us.dp1.chess.tournament.practice.b;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ReadingClubRepository extends CrudRepository<ReadingClub, Integer> {

    @Query("SELECT -1 FROM ReadingClub c")
    List<Object[]> findOverdueCounts(Integer clubId, LocalDate referenceDate);
    // TODO: Implement to solve Test B6

}
