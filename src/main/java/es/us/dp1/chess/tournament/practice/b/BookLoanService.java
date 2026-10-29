package es.us.dp1.chess.tournament.practice.b;

import java.util.List;
import java.util.Optional;

public class BookLoanService {

    BookLoanRepository repo;

    public BookLoanService(BookLoanRepository repo) {
        this.repo = repo;
    }

    public List<BookLoan> findAll() {
        // TODO: Implement to solve Test B5
        return null;
    }

    public Optional<BookLoan> findById(Integer id) {
        // TODO: Implement to solve Test B5
        return null;
    }

    public BookLoan save(BookLoan loan) throws InvalidLoanPeriodException {
        // TODO: Implement to solve Test B5
        return null;
    }

}
