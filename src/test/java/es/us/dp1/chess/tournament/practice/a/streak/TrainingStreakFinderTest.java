package es.us.dp1.chess.tournament.practice.a.streak;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class TrainingStreakFinderTest {

    // This is your SUT:
    TrainingStreakFinder algorithm = new ValidTrainingStreakFinder();

    // One test per rule of the specification. Only assertions are used (never try/catch),
    // because the TestA8 dispatcher only counts an AssertionFailedError as a detected bug.

    @Test
    public void onATieReturnsTheEarliestRun() throws Exception {
        assertArrayEquals(new int[] { 0, 1 }, algorithm.findLongestStreak(new int[] { 30, 45, 10, 60, 60, 20 }, 30));
    }

    @Test
    public void daysEqualToTheGoalCount() throws Exception {
        assertArrayEquals(new int[] { 1, 3 }, algorithm.findLongestStreak(new int[] { 10, 40, 40, 40, 5, 50 }, 40));
    }

    @Test
    public void returnsTheLongestRunEvenIfItIsNotTheFirst() throws Exception {
        assertArrayEquals(new int[] { 2, 4 }, algorithm.findLongestStreak(new int[] { 50, 0, 50, 60, 70 }, 30));
    }

    @Test
    public void aSingleDayCanBeTheStreak() throws Exception {
        assertArrayEquals(new int[] { 1, 1 }, algorithm.findLongestStreak(new int[] { 10, 90, 10 }, 60));
    }

    @Test
    public void returnsAnEmptyArrayIfNoDayReachesTheGoal() throws Exception {
        assertArrayEquals(new int[0], algorithm.findLongestStreak(new int[] { 5, 10 }, 30));
    }

    @Test
    public void nullMinutesThrowsEmptyTrainingLog() {
        assertThrows(EmptyTrainingLogException.class, () -> algorithm.findLongestStreak(null, 30));
    }

    @Test
    public void emptyMinutesThrowsEmptyTrainingLog() {
        assertThrows(EmptyTrainingLogException.class, () -> algorithm.findLongestStreak(new int[0], 30));
    }

    @Test
    public void zeroGoalThrowsInvalidTrainingData() {
        assertThrows(InvalidTrainingDataException.class, () -> algorithm.findLongestStreak(new int[] { 30, 40 }, 0));
    }

    @Test
    public void negativeGoalThrowsInvalidTrainingData() {
        assertThrows(InvalidTrainingDataException.class, () -> algorithm.findLongestStreak(new int[] { 30, 40 }, -5));
    }

    @Test
    public void negativeMinutesThrowsInvalidTrainingData() {
        assertThrows(InvalidTrainingDataException.class,
                () -> algorithm.findLongestStreak(new int[] { 30, -1, 40 }, 30));
    }

    public void setAlgorithm(TrainingStreakFinder value){
        this.algorithm=value;
    }

}
