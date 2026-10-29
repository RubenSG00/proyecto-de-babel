package es.us.dp1.chess.tournament.practice.a;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.practice.a.streak.CarelessTrainingStreakFinder;
import es.us.dp1.chess.tournament.practice.a.streak.EmptyTrainingLogException;
import es.us.dp1.chess.tournament.practice.a.streak.InvalidTrainingDataException;
import es.us.dp1.chess.tournament.practice.a.streak.LateTrainingStreakFinder;
import es.us.dp1.chess.tournament.practice.a.streak.StrictTrainingStreakFinder;
import es.us.dp1.chess.tournament.practice.a.streak.TrainingStreakFinder;
import es.us.dp1.chess.tournament.practice.a.streak.TrainingStreakFinderTest;
import es.us.dp1.chess.tournament.practice.a.streak.ValidTrainingStreakFinder;

// Same dispatcher as the mock's Test8: it runs every @Test method of TrainingStreakFinderTest
// against each implementation; none may fail with the valid one and at least one must fail
// (with an AssertionFailedError) with each faulty one.
public class TestA8 extends ReflexiveTest{
    public class WrapperAlgorithm implements TrainingStreakFinder {
        private TrainingStreakFinder algorithm;
        private int numRuns;

        public WrapperAlgorithm(TrainingStreakFinder algorithm) {
            this.algorithm = algorithm;
            this.numRuns = 0;
        }

        public int getNumRuns() {
            return numRuns;
        }

        @Override
        public int[] findLongestStreak(int[] minutes, int goal) throws EmptyTrainingLogException, InvalidTrainingDataException {
                    numRuns++;
                    return algorithm.findLongestStreak(minutes, goal);
        }
    }



    @ParameterizedTest
    @MethodSource("provideAlgorithmsAndExpectedResults")
    public void testTrainingStreakFinderAlgorithm(TrainingStreakFinder alg, boolean shouldFail){
        // Configure SUT:
        TrainingStreakFinderTest cdaTest=new TrainingStreakFinderTest();
        WrapperAlgorithm wrapper = new WrapperAlgorithm(alg);
        cdaTest.setAlgorithm(wrapper);
        int numberOfExecutedTestMethods=0;
        // ExecuteTests
        numberOfExecutedTestMethods=executeTests(cdaTest, shouldFail);
        if(numberOfExecutedTestMethods<1)
            fail("You have not specified any test method!");
        if(wrapper.getNumRuns() < 1)
            fail("The SUT has not been executed in the test!");
    }

    private void executeAfterEach(TrainingStreakFinderTest cdaTest) {
        Method[] methods=cdaTest.getClass().getDeclaredMethods();
        for(Method method:methods){
            if(isMethodAnnotatedWithAfterEach(method)){
                try {
                    method.invoke(cdaTest);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    System.out.println("Error while trying to invoke method:"+method.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    private int executeTests(TrainingStreakFinderTest cdaTest, boolean shouldFail) {
        int executed=0;
        Method[] methods=cdaTest.getClass().getDeclaredMethods();
        boolean failDetected=false;
        String message="No test method detected the faulty implementation of the algorithm";
        for(Method method:methods){
            if(isMethodAnnotatedWithTest(method)){
                try {
                    executed++;
                    executeBeforeEach(cdaTest);
                    method.invoke(cdaTest);
                    executeAfterEach(cdaTest);
                }catch(AssertionError assertionError){
                    failDetected=true;
                    message="The test method named "+method.getName()+" failed (and should not)! AsssertionError: "+assertionError.getMessage();
                } catch(InvocationTargetException e){
                    if(e.getTargetException() instanceof org.opentest4j.AssertionFailedError){
                        failDetected=true;
                        message="The test method named "+method.getName()+" failed (and should not)! AsssertionError: "
                                    +((org.opentest4j.AssertionFailedError)e.getTargetException()).getMessage();
                    }else
                        System.out.println("Error while trying to invoke method:"+method.getName());
                }catch (IllegalAccessException | IllegalArgumentException  e) {
                    System.out.println("Error while trying to invoke method:"+method.getName());
                }
            }
        }
        if(failDetected!=shouldFail)
            fail(message);
        return executed;
    }

    private void executeBeforeEach(TrainingStreakFinderTest cdaTest) {
        Method[] methods=cdaTest.getClass().getDeclaredMethods();
        for(Method method:methods){
            if(isMethodAnnotatedWithBeforeEach(method)){
                try {
                    method.invoke(cdaTest);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    System.out.println("Error while trying to invoke method:"+method.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public static Stream<Arguments> provideAlgorithmsAndExpectedResults(){
        return Stream.of(
            Arguments.of(new ValidTrainingStreakFinder(), false),
            Arguments.of(new StrictTrainingStreakFinder(), true),
            Arguments.of(new LateTrainingStreakFinder(), true),
            Arguments.of(new CarelessTrainingStreakFinder(), true)
        );
    }

}
