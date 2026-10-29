package es.us.dp1.chess.tournament.practice.x.queries;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface MusicianRepository extends CrudRepository<Musician, Integer> {

    // Test X05

    // Positional parameter: ?1 is the first parameter of the method
    @Query("SELECT m FROM Musician m WHERE m.instrument = ?1 ORDER BY m.name")
    List<Musician> musiciansPlaying(String instrument);

    // Named parameters bound with @Param: the order of the method parameters no longer matters.
    // BETWEEN includes both ends.
    @Query("SELECT m FROM Musician m WHERE m.salary BETWEEN :low AND :high ORDER BY m.salary DESC")
    List<Musician> musiciansEarningBetween(@Param("low") Integer low, @Param("high") Integer high);

    @Query("SELECT m FROM Musician m WHERE m.name LIKE CONCAT(:prefix, '%') ORDER BY m.name")
    List<Musician> musiciansNamedLike(@Param("prefix") String prefix);

    // "= NULL" is never true: IS NULL
    @Query("SELECT m FROM Musician m WHERE m.agent IS NULL ORDER BY m.name")
    List<Musician> musiciansWithoutAgent();

    // Test X06

    @Query("SELECT m FROM Musician m WHERE m.bands IS EMPTY")
    List<Musician> musiciansWithoutBand();

    @Query("SELECT m FROM Musician m WHERE m.bands IS NOT EMPTY")
    List<Musician> musiciansWithBand();

    // DISTINCT: a musician in two bands of the city would be two rows of the join (Hibernate 6
    // already removes duplicate entities from the result, but not duplicate scalar values)
    @Query("SELECT DISTINCT m FROM Musician m, IN (m.bands) b WHERE b.city = :city")
    List<Musician> musiciansPlayingInCity(@Param("city") String city);

    // Self-join: m2 is the musician used as reference
    @Query("SELECT DISTINCT m1 FROM Musician m1, Musician m2 WHERE m1.salary > m2.salary AND m2.name = :name")
    List<Musician> musiciansEarningMoreThan(@Param("name") String name);

    @Query("SELECT COUNT(m) FROM Musician m JOIN m.bands b WHERE b.id = :bandId")
    Long countMusiciansOfBand(@Param("bandId") Integer bandId);

    @Query("SELECT DISTINCT m.instrument FROM Musician m JOIN m.bands b WHERE b.city = :city ORDER BY m.instrument")
    List<String> instrumentsPlayedInCity(@Param("city") String city);

}
