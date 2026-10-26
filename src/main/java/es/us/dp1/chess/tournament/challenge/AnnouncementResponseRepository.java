package es.us.dp1.chess.tournament.challenge;

import java.util.Optional;

public interface AnnouncementResponseRepository {
    Optional<AnnouncementResponse> findById(Integer id);
}
