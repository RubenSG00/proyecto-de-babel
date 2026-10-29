package es.us.dp1.chess.tournament.practice.c;

public class InvalidFlightTimesException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidFlightTimesException() {
        super("A landed flight needs a departure, and its arrival must be after it");
    }

}
