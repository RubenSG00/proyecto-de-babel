package es.us.dp1.chess.tournament.practice.x.queries;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

// Derived queries: Spring Data builds the query from the method name, so the property names
// (city, price, date, soldOut, title, genre) must match the attributes exactly.
public interface ConcertRepository extends CrudRepository<Concert, Integer> {

    List<Concert> findByCity(String city);

    List<Concert> findByPriceLessThanEqualOrderByPriceAsc(Integer maxPrice);

    // Between includes both ends
    List<Concert> findByDateBetween(LocalDate from, LocalDate to);

    List<Concert> findBySoldOutFalse();

    List<Concert> findByTitleContainingIgnoreCase(String text);

    List<Concert> findByGenreIn(Collection<MusicGenre> genres);

    // Optional: zero or one result
    Optional<Concert> findByTitle(String title);

    // Test X04: After and LessThan are strict (LessThanEqual is not)

    List<Concert> findByCityAndDateAfterOrderByDateAsc(String city, LocalDate from);

    List<Concert> findByPriceLessThanAndSoldOutFalse(Integer maxPrice);

    List<Concert> findByGenreOrCity(MusicGenre genre, String city);

    List<Concert> findByTitleStartingWithOrderByTitleDesc(String prefix);

}
