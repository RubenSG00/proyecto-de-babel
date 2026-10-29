package es.us.dp1.chess.tournament.practice.a;

import java.util.Optional;

public interface ProgramNoticeRepository {
    Optional<ProgramNotice> findById(Integer id);
}
