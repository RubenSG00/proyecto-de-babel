package es.us.dp1.chess.tournament.practice.c;

public class InvalidScoreThresholdException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidScoreThresholdException(Integer minScore) {
        super("The minimum score cannot be negative: " + minScore);
    }

}
