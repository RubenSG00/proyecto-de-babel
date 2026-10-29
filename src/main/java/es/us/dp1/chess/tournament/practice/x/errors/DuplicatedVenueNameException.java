package es.us.dp1.chess.tournament.practice.x.errors;

public class DuplicatedVenueNameException extends Exception {

    private static final long serialVersionUID = 1L;

    public DuplicatedVenueNameException(String name) {
        super("There is already a venue called " + name);
    }

}
