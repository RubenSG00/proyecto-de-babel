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

    // TODO: Replace the placeholder queries to solve Test X06

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansWithoutBand();

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansWithBand();

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansPlayingInCity(String city);

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansEarningMoreThan(String name);

    @Query("SELECT COUNT(m) FROM Musician m WHERE m.id < 0")
    Long countMusiciansOfBand(Integer bandId);

}
