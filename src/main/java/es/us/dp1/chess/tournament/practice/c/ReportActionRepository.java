package es.us.dp1.chess.tournament.practice.c;

import java.util.Optional;

public interface ReportActionRepository {
    Optional<ReportAction> findById(Integer id);
}
