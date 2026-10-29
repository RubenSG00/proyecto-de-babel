package es.us.dp1.chess.tournament.practice.c;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface SafetyReportRepository extends CrudRepository<SafetyReport, Integer> {
    Optional<SafetyReport> findById(Integer id);
}
