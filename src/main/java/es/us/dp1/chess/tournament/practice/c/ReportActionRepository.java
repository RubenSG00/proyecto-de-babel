package es.us.dp1.chess.tournament.practice.c;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ReportActionRepository extends CrudRepository<ReportAction, Integer> {
    Optional<ReportAction> findById(Integer id);
}
