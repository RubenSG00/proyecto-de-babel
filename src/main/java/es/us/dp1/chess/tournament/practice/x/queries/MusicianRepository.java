package es.us.dp1.chess.tournament.practice.x.queries;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MusicianRepository extends CrudRepository<Musician, Integer> {

    // TODO: Replace the placeholder queries to solve Test X05

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansPlaying(String instrument);

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansEarningBetween(Integer low, Integer high);

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
    List<Musician> musiciansNamedLike(String prefix);

    @Query("SELECT m FROM Musician m WHERE m.id < 0")
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
