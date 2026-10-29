package es.us.dp1.chess.tournament.practice.b;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Class-level @Transactional: every method is transactional; the method-level annotations
// below override it for those methods (read-only reads, rollback for the checked exception)
@Service
@Transactional
public class BookLoanService {

    private static final long MAX_LOAN_DAYS = 21;

    BookLoanRepository repo;

    @Autowired
    public BookLoanService(BookLoanRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<BookLoan> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<BookLoan> findById(Integer id) {
        return repo.findById(id);
    }

    @Transactional(rollbackFor = InvalidLoanPeriodException.class)
    public BookLoan save(BookLoan loan) throws InvalidLoanPeriodException {
        if (loan.getLoanDate() == null || loan.getDueDate() == null)
            throw new InvalidLoanPeriodException();
        long days = ChronoUnit.DAYS.between(loan.getLoanDate(), loan.getDueDate());
        // negative: due before the loan date; exactly 21 days is still valid
        if (days < 0 || days > MAX_LOAN_DAYS)
            throw new InvalidLoanPeriodException();
        return repo.save(loan);
    }

}
