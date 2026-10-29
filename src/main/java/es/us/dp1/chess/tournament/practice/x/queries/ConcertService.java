package es.us.dp1.chess.tournament.practice.x.queries;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConcertService {

    private final ConcertRepository repo;

    @Autowired
    public ConcertService(ConcertRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Concert> findUpcomingInCity(String city, LocalDate from) {
        return repo.findByCityAndDateAfterOrderByDateAsc(city, from);
    }

    @Transactional(readOnly = true)
    public List<Concert> findAffordableNotSoldOut(Integer maxPrice) {
        return repo.findByPriceLessThanAndSoldOutFalse(maxPrice);
    }

    @Transactional(readOnly = true)
    public List<Concert> findByGenreOrCity(MusicGenre genre, String city) {
        return repo.findByGenreOrCity(genre, city);
    }

    @Transactional(readOnly = true)
    public List<Concert> findStartingWith(String prefix) {
        return repo.findByTitleStartingWithOrderByTitleDesc(prefix);
    }

}
