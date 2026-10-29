package es.us.dp1.chess.tournament.practice.a.streak;

public interface TrainingStreakFinder {

    /**
     * Returns [first, last] (first <= last), the longest run of consecutive days whose
     * minutes are greater than or equal to goal. On a tie, the earliest run (smallest
     * first) is returned. If no day reaches the goal, an empty array is returned.
     *
     * @throws EmptyTrainingLogException if minutes is null or empty.
     * @throws InvalidTrainingDataException if goal is zero or negative, or any value of
     *         minutes is negative.
     */
    int[] findLongestStreak(int[] minutes, int goal) throws EmptyTrainingLogException, InvalidTrainingDataException;

}
