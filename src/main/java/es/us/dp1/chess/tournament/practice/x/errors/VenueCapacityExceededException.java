package es.us.dp1.chess.tournament.practice.x.errors;

public class VenueCapacityExceededException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public VenueCapacityExceededException(Venue venue) {
        super("Venue " + venue.getName() + " only admits " + venue.getCapacity() + " people");
    }

}
