package es.us.dp1.chess.tournament.practice.a;

import java.util.Optional;

public interface NoticeAcknowledgementRepository {
    Optional<NoticeAcknowledgement> findById(Integer id);
}
