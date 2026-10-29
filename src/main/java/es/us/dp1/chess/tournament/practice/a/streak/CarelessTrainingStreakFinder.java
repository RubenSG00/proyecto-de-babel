package es.us.dp1.chess.tournament.practice.a.streak;

public class CarelessTrainingStreakFinder implements TrainingStreakFinder {

    // Bug: does not validate that the goal is positive.
    @Override
    public int[] findLongestStreak(int[] minutes, int goal) throws EmptyTrainingLogException, InvalidTrainingDataException {
        if (minutes == null || minutes.length == 0) {
            throw new EmptyTrainingLogException();
        }
        for (int m : minutes) {
            if (m < 0) {
                throw new InvalidTrainingDataException();
            }
        }
        int bestFirst = -1, bestLength = 0, currentFirst = -1;
        for (int i = 0; i < minutes.length; i++) {
            if (minutes[i] >= goal) {
                if (currentFirst < 0) {
                    currentFirst = i;
                }
                int length = i - currentFirst + 1;
                if (length > bestLength) {
                    bestLength = length;
                    bestFirst = currentFirst;
                }
            } else {
                currentFirst = -1;
            }
        }
        if (bestLength == 0) {
            return new int[0];
        }
        return new int[] { bestFirst, bestFirst + bestLength - 1 };
    }

}
