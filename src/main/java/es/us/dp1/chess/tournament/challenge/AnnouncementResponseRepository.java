package es.us.dp1.chess.tournament.challenge;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface AnnouncementResponseRepository extends CrudRepository<AnnouncementResponse, Integer> {
    Optional<AnnouncementResponse> findById(Integer id);
}
