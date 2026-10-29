package es.us.dp1.chess.tournament.practice.c;

import java.util.Optional;

public interface SafetyReportRepository {
    Optional<SafetyReport> findById(Integer id);
}
