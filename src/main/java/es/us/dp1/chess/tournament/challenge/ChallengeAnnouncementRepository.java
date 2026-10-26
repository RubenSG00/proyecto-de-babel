package es.us.dp1.chess.tournament.challenge;

import java.util.Optional;

public interface ChallengeAnnouncementRepository {
    Optional<ChallengeAnnouncement> findById(Integer id);
}
