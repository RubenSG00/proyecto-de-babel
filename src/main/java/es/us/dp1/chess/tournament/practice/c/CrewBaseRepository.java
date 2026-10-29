package es.us.dp1.chess.tournament.practice.c;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CrewBaseRepository extends CrudRepository<CrewBase, Integer> {

    // WHERE filters the rows (landed, not diverted, flown by the pilot) before grouping;
    // HAVING filters the groups by their total, which only exists after aggregating.
    @Query("SELECT p, SUM((CASE WHEN f.captain = p THEN 3 ELSE 1 END) "
            + "+ (CASE WHEN f.distanceKm >= 3000 THEN 1 ELSE 0 END)) "
            + "FROM CrewBase b JOIN b.pilots p JOIN b.flights f "
            + "WHERE b.id = :baseId AND f.arrival IS NOT NULL AND f.diverted = false "
            + "AND (f.captain = p OR f.firstOfficer = p) "
            + "GROUP BY p "
            + "HAVING SUM((CASE WHEN f.captain = p THEN 3 ELSE 1 END) "
            + "+ (CASE WHEN f.distanceKm >= 3000 THEN 1 ELSE 0 END)) >= :minScore "
            + "ORDER BY 2 DESC, p.username ASC")
    List<Object[]> findPilotScores(@Param("baseId") Integer baseId, @Param("minScore") Integer minScore);

}
