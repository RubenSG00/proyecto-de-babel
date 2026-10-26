package es.us.dp1.chess.tournament.challenge.matchmaking;

public interface ClosestRatingPairFinder {

    /**
     * Finds, within an array of ratings, the pair of positions [i, j] (with i < j)
     * whose rating difference is minimal. In case of a tie between several pairs
     * with the same minimal difference, the pair with the smallest sum of indices
     * (i + j) must be returned.
     *
     * @throws InsufficientPlayersException if ratings has fewer than 2 elements.
     * @throws InvalidRatingException if any rating is negative.
     */
    int[] findClosestPair(int[] ratings) throws InsufficientPlayersException, InvalidRatingException;

}
