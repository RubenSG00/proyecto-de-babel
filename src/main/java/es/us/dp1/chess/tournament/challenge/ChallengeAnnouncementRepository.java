package es.us.dp1.chess.tournament.challenge;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ChallengeAnnouncementRepository extends CrudRepository<ChallengeAnnouncement, Integer> {
    Optional<ChallengeAnnouncement> findById(Integer id);
}
