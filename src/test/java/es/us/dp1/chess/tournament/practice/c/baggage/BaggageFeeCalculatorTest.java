package es.us.dp1.chess.tournament.practice.c.baggage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class BaggageFeeCalculatorTest {

    // This is your SUT:
    BaggageFeeCalculator calculator = new ValidBaggageFeeCalculator();

    // Please specify as many tests as you need, as public void methods annotated with
    // @Test and without parameters, using structures similar to this:
    //
    // @Test
    // public void someTest() throws Exception {
    //     // Arrangement / Configuration / Fixture
    //     // Act / SUT invocation
    //     // Assertion
    // }



    public void setCalculator(BaggageFeeCalculator value){
        this.calculator=value;
    }

}
