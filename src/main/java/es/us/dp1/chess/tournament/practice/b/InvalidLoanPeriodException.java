package es.us.dp1.chess.tournament.practice.b;

public class InvalidLoanPeriodException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidLoanPeriodException() {
        super("A loan needs a loan date and a due date, at most 21 days later.");
    }

}
