package es.us.dp1.chess.tournament.matchmaking;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import es.us.dp1.chess.tournament.challenge.matchmaking.ClosestRatingPairFinder;
import es.us.dp1.chess.tournament.challenge.matchmaking.InsufficientPlayersException;
import es.us.dp1.chess.tournament.challenge.matchmaking.InvalidRatingException;
import es.us.dp1.chess.tournament.challenge.matchmaking.ValidClosestRatingPairFinder;


public class ClosestRatingPairFinderTest {

    // This is your SUT:
    ClosestRatingPairFinder algorithm = new ValidClosestRatingPairFinder();

    // The Test8 dispatcher only counts an AssertionFailedError as a detected bug. assertThrows
    // turns an unexpected exception (e.g. the ArrayIndexOutOfBoundsException of the reckless
    // implementation with an empty array) into an AssertionFailedError; a try/catch with fail()
    // would let it escape and the bug would go undetected.

    @Test
    public void findsTheClosestPair() throws Exception {
        int[] result = algorithm.findClosestPair(new int[] { 1000, 1200, 1050 });
        assertArrayEquals(new int[] { 0, 2 }, result);
    }

    @Test
    public void findsAdjacentPair() throws Exception {
        int[] result = algorithm.findClosestPair(new int[] { 1500, 1510, 1700 });
        assertArrayEquals(new int[] { 0, 1 }, result);
    }

    @Test
    public void worksWithExactlyTwoPlayers() throws Exception {
        int[] result = algorithm.findClosestPair(new int[] { 1800, 1200 });
        assertArrayEquals(new int[] { 0, 1 }, result);
    }

    @Test
    public void onATieReturnsThePairWithTheSmallestSumOfIndices() throws Exception {
        int[] result = algorithm.findClosestPair(new int[] { 0, 10, 14, 100, 200, 4 });
        assertArrayEquals(new int[] { 1, 2 }, result);
    }

    @Test
    public void acceptsZeroAsARating() throws Exception {
        int[] result = algorithm.findClosestPair(new int[] { 0, 500, 3 });
        assertArrayEquals(new int[] { 0, 2 }, result);
    }

    @Test
    public void emptyArrayThrowsInsufficientPlayers() {
        assertThrows(InsufficientPlayersException.class, () -> algorithm.findClosestPair(new int[] {}));
    }

    @Test
    public void oneElementArrayThrowsInsufficientPlayers() {
        assertThrows(InsufficientPlayersException.class, () -> algorithm.findClosestPair(new int[] { 1500 }));
    }

    @Test
    public void negativeRatingThrowsInvalidRating() {
        assertThrows(InvalidRatingException.class,
                () -> algorithm.findClosestPair(new int[] { 1500, -1, 1600 }));
    }

    public void setAlgorithm(ClosestRatingPairFinder value){
        this.algorithm=value;
    }

}
