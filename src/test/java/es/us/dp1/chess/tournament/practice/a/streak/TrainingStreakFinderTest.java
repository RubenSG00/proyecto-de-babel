package es.us.dp1.chess.tournament.practice.a.streak;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class TrainingStreakFinderTest {

    // This is your SUT:
    TrainingStreakFinder algorithm = new ValidTrainingStreakFinder();

    // Please specify as many tests as you need, as public void methods annotated with
    // @Test and without parameters, using structures similar to this:
    //
    // @Test
    // public void someTest() throws Exception {
    //     // Arrangement / Configuration / Fixture
    //     // Act / SUT invocation
    //     // Assertion
    // }



    public void setAlgorithm(TrainingStreakFinder value){
        this.algorithm=value;
    }

}
