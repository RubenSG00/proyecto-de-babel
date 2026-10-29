package es.us.dp1.chess.tournament.practice.b;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.Test1;

@ExtendWith(MockitoExtension.class)
public class TestB5 extends ReflexiveTest {

    @Mock
    BookLoanRepository blr;

    BookLoanService bls;

    private static final LocalDate DAY = LocalDate.of(2026, 9, 1);

    @BeforeEach
    public void configuration() {
        bls = new BookLoanService(blr);
    }

    @Test
    public void testB5TransactionsAreDeclaredAtClassLevel() throws NoSuchMethodException {
        assertTrue(classIsAnnotatedWith(BookLoanService.class, Service.class),
                "BookLoanService is not annotated as a Spring service");
        assertNotNull(BookLoanService.class.getAnnotation(Transactional.class),
                "BookLoanService must be annotated with Spring's @Transactional at class level");
    }

    @Test
    public void testB5CheckReadOnlyAndRollback() throws NoSuchMethodException {
        Transactional findAll = BookLoanService.class.getDeclaredMethod("findAll").getAnnotation(Transactional.class);
        Transactional findById = BookLoanService.class.getDeclaredMethod("findById", Integer.class)
                .getAnnotation(Transactional.class);
        assertTrue(findAll != null && findAll.readOnly(), "findAll must be a read-only transaction");
        assertTrue(findById != null && findById.readOnly(), "findById must be a read-only transaction");
        checkTransactionalRollback(BookLoanService.class, "save", Arrays.array(BookLoan.class),
                InvalidLoanPeriodException.class);
    }

    @Test
    public void testB5CanGetAllLoansAndOneLoan() {
        BookLoan l = new BookLoan();
        when(blr.findAll()).thenReturn(List.of(l));
        when(blr.findById(15)).thenReturn(Optional.of(l));
        assertEquals(List.of(l), bls.findAll());
        verify(blr).findAll();
        Optional<BookLoan> found = bls.findById(15);
        verify(blr).findById(15);
        assertNotNull(found, "findById must not return null");
        assertSame(l, found.get());
    }

    @Test
    public void testB5CanSaveLoansOfUpTo21Days() {
        when(blr.save(any(BookLoan.class))).thenAnswer(i -> i.getArgument(0));
        for (int days : new int[] { 0, 1, 21 }) {
            BookLoan l = createLoan(DAY, DAY.plusDays(days));
            assertDoesNotThrow(() -> bls.save(l), "A loan of " + days + " days must be saved");
            verify(blr).save(l);
        }
    }

    @Test
    public void testB5CannotSaveLoansLongerThan21Days() {
        BookLoan l = createLoan(DAY, DAY.plusDays(22));
        assertThrows(InvalidLoanPeriodException.class, () -> bls.save(l));
        verify(blr, never()).save(l);
    }

    @Test
    public void testB5CannotSaveLoansDueBeforeTheyStart() {
        BookLoan l = createLoan(DAY, DAY.minusDays(1));
        assertThrows(InvalidLoanPeriodException.class, () -> bls.save(l));
        verify(blr, never()).save(l);
    }

    @Test
    public void testB5CannotSaveLoansWithoutDates() {
        BookLoan noLoanDate = createLoan(null, DAY);
        BookLoan noDueDate = createLoan(DAY, null);
        assertThrows(InvalidLoanPeriodException.class, () -> bls.save(noLoanDate));
        assertThrows(InvalidLoanPeriodException.class, () -> bls.save(noDueDate));
        verify(blr, never()).save(any(BookLoan.class));
    }

    private BookLoan createLoan(LocalDate loanDate, LocalDate dueDate) {
        BookLoan l = new BookLoan();
        l.setBookTitle("Dune");
        l.setBorrower(Test1.createUser("borrower"));
        l.setLoanDate(loanDate);
        l.setDueDate(dueDate);
        return l;
    }

}
