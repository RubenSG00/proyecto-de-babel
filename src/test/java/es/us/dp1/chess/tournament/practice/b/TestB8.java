package es.us.dp1.chess.tournament.practice.b;

import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import es.us.dp1.chess.tournament.ReflexiveTest;
import es.us.dp1.chess.tournament.practice.b.fees.EagerLateFeeCalculator;
import es.us.dp1.chess.tournament.practice.b.fees.InvalidDaysLateException;
import es.us.dp1.chess.tournament.practice.b.fees.LateFeeCalculator;
import es.us.dp1.chess.tournament.practice.b.fees.LateFeeCalculatorTest;
import es.us.dp1.chess.tournament.practice.b.fees.LooseCapLateFeeCalculator;
import es.us.dp1.chess.tournament.practice.b.fees.RoundingLateFeeCalculator;
import es.us.dp1.chess.tournament.practice.b.fees.ValidLateFeeCalculator;

// Same dispatcher as the mock's Test8: it runs every @Test method of LateFeeCalculatorTest
// against each implementation; none may fail with the valid one and at least one must fail
// (with an AssertionFailedError) with each faulty one.
public class TestB8 extends ReflexiveTest{
    public class WrapperAlgorithm implements LateFeeCalculator {
        private LateFeeCalculator algorithm;
        private int numRuns;

        public WrapperAlgorithm(LateFeeCalculator algorithm) {
            this.algorithm = algorithm;
            this.numRuns = 0;
        }

        public int getNumRuns() {
            return numRuns;
        }

        @Override
        public int calculateFee(int daysLate, boolean premiumMember) throws InvalidDaysLateException {
                    numRuns++;
                    return algorithm.calculateFee(daysLate, premiumMember);
        }
    }



    @ParameterizedTest
    @MethodSource("provideAlgorithmsAndExpectedResults")
    public void testLateFeeCalculatorAlgorithm(LateFeeCalculator alg, boolean shouldFail){
        // Configure SUT:
        LateFeeCalculatorTest cdaTest=new LateFeeCalculatorTest();
        WrapperAlgorithm wrapper = new WrapperAlgorithm(alg);
        cdaTest.setCalculator(wrapper);
        int numberOfExecutedTestMethods=0;
        // ExecuteTests
        numberOfExecutedTestMethods=executeTests(cdaTest, shouldFail);
        if(numberOfExecutedTestMethods<1)
            fail("You have not specified any test method!");
        if(wrapper.getNumRuns() < 1)
            fail("The SUT has not been executed in the test!");
    }

    private void executeAfterEach(LateFeeCalculatorTest cdaTest) {
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

    private int executeTests(LateFeeCalculatorTest cdaTest, boolean shouldFail) {
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

    private void executeBeforeEach(LateFeeCalculatorTest cdaTest) {
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
            Arguments.of(new ValidLateFeeCalculator(), false),
            Arguments.of(new EagerLateFeeCalculator(), true),
            Arguments.of(new RoundingLateFeeCalculator(), true),
            Arguments.of(new LooseCapLateFeeCalculator(), true)
        );
    }

}
