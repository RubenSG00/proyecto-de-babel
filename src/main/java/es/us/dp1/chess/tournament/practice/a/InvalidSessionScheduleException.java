package es.us.dp1.chess.tournament.practice.a;

public class InvalidSessionScheduleException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidSessionScheduleException() {
        super("A finished session must have a start, and its finish must be after its start.");
    }

}
