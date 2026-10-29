package es.us.dp1.chess.tournament.practice.c;

public class InvalidCrewException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidCrewException() {
        super("The captain and the first officer must be two different users");
    }

}
