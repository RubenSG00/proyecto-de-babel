package es.us.dp1.chess.tournament.practice.x.transactions;

public class WorkshopFullException extends Exception {

    private static final long serialVersionUID = 1L;

    public WorkshopFullException() {
        super("The workshop is full");
    }

}
