package es.us.dp1.chess.tournament.practice.b;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BookLoanRepository extends CrudRepository<BookLoan, Integer> {
    public List<BookLoan> findAll();
}
