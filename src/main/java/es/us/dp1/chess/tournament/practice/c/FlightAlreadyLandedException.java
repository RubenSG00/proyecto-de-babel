package es.us.dp1.chess.tournament.practice.c;

public class FlightAlreadyLandedException extends Exception {

    private static final long serialVersionUID = 1L;

    public FlightAlreadyLandedException() {
        super("The flight has already landed");
    }

}
