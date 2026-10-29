package es.us.dp1.chess.tournament.practice.b.fees;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class LateFeeCalculatorTest {

    // This is your SUT:
    LateFeeCalculator calculator = new ValidLateFeeCalculator();

    // Please specify as many tests as you need, as public void methods annotated with
    // @Test and without parameters, using structures similar to this:
    //
    // @Test
    // public void someTest() throws Exception {
    //     // Arrangement / Configuration / Fixture
    //     // Act / SUT invocation
    //     // Assertion
    // }



    public void setCalculator(LateFeeCalculator value){
        this.calculator=value;
    }

}
