package es.us.dp1.chess.tournament.challenge.matchmaking;

public class NaiveClosestRatingPairFinder implements ClosestRatingPairFinder {

    // Bug: on a tie, keeps the first pair found instead of the one with the
    // smallest sum of indices.
    @Override
    public int[] findClosestPair(int[] ratings) throws InsufficientPlayersException, InvalidRatingException {
        if (ratings == null || ratings.length < 2) {
            throw new InsufficientPlayersException();
        }
        for (int rating : ratings) {
            if (rating < 0) {
                throw new InvalidRatingException();
            }
        }
        int bestI = 0, bestJ = 1;
        int bestDiff = Math.abs(ratings[0] - ratings[1]);
        for (int i = 0; i < ratings.length; i++) {
            for (int j = i + 1; j < ratings.length; j++) {
                int diff = Math.abs(ratings[i] - ratings[j]);
                if (diff < bestDiff) {
                    bestDiff = diff;
                    bestI = i;
                    bestJ = j;
                }
            }
        }
        return new int[] { bestI, bestJ };
    }

}
