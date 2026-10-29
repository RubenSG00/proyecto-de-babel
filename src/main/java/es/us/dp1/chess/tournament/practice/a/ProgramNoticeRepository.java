package es.us.dp1.chess.tournament.practice.a;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ProgramNoticeRepository extends CrudRepository<ProgramNotice, Integer> {
    Optional<ProgramNotice> findById(Integer id);
}
