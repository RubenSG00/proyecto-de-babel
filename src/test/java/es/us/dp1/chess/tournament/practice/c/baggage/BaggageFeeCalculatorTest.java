package es.us.dp1.chess.tournament.practice.c.baggage;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class BaggageFeeCalculatorTest {

    // This is your SUT:
    BaggageFeeCalculator calculator = new ValidBaggageFeeCalculator();

    // Equivalence classes (economy/business x free/extra bags x under/at/over the limit)
    // and boundary values (0, 23/24, 32/33 and 50/51 kg).

    @Test
    public void noBagsCostNothing() throws Exception {
        assertEquals(0, calculator.calculateFee(null, false));
        assertEquals(0, calculator.calculateFee(new int[0], true));
    }

    @Test
    public void economyFreeBagAndExtraBags() throws Exception {
        assertEquals(0, calculator.calculateFee(new int[] { 20 }, false));
        assertEquals(50, calculator.calculateFee(new int[] { 20, 20 }, false));
        assertEquals(100, calculator.calculateFee(new int[] { 5, 5, 5 }, false));
    }

    @Test
    public void economyWeightLimitIs23Kg() throws Exception {
        assertEquals(0, calculator.calculateFee(new int[] { 23 }, false));
        assertEquals(75, calculator.calculateFee(new int[] { 24 }, false));
        assertEquals(200, calculator.calculateFee(new int[] { 24, 30 }, false));
    }

    @Test
    public void businessHasTwoFreeBags() throws Exception {
        assertEquals(0, calculator.calculateFee(new int[] { 30, 31 }, true));
        assertEquals(50, calculator.calculateFee(new int[] { 10, 10, 10 }, true));
    }

    @Test
    public void businessWeightLimitIs32Kg() throws Exception {
        assertEquals(0, calculator.calculateFee(new int[] { 32 }, true));
        assertEquals(75, calculator.calculateFee(new int[] { 33 }, true));
        assertEquals(125, calculator.calculateFee(new int[] { 33, 10, 10 }, true));
    }

    @Test
    public void validBoundaryWeights() throws Exception {
        assertEquals(0, calculator.calculateFee(new int[] { 1 }, false));
        assertEquals(75, calculator.calculateFee(new int[] { 50 }, false));
    }

    @Test
    public void bagsOfZeroKgOrLessAreInvalid() {
        assertThrows(InvalidBagException.class, () -> calculator.calculateFee(new int[] { 0 }, false));
        assertThrows(InvalidBagException.class, () -> calculator.calculateFee(new int[] { 10, -3 }, true));
    }

    @Test
    public void bagsOverFiftyKgAreInvalid() {
        assertThrows(InvalidBagException.class, () -> calculator.calculateFee(new int[] { 51 }, false));
        assertThrows(InvalidBagException.class, () -> calculator.calculateFee(new int[] { 20, 51 }, true));
    }

    public void setCalculator(BaggageFeeCalculator value){
        this.calculator=value;
    }

}
