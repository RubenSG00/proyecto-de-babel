package es.us.dp1.chess.tournament.practice.a;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface NoticeAcknowledgementRepository extends CrudRepository<NoticeAcknowledgement, Integer> {
    Optional<NoticeAcknowledgement> findById(Integer id);
}
