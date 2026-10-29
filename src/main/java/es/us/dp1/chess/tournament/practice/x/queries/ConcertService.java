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
        // TODO: Implement to solve Test X04
        return null;
    }

    @Transactional(readOnly = true)
    public List<Concert> findAffordableNotSoldOut(Integer maxPrice) {
        // TODO: Implement to solve Test X04
        return null;
    }

    @Transactional(readOnly = true)
    public List<Concert> findByGenreOrCity(MusicGenre genre, String city) {
        // TODO: Implement to solve Test X04
        return null;
    }

    @Transactional(readOnly = true)
    public List<Concert> findStartingWith(String prefix) {
        // TODO: Implement to solve Test X04
        return null;
    }

}
