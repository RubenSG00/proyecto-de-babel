package es.us.dp1.chess.tournament.practice.b.fees;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class LateFeeCalculatorTest {

    // This is your SUT:
    LateFeeCalculator calculator = new ValidLateFeeCalculator();

    // Boundary-value tests: just below, on and just above each limit of the specification.

    @Test
    public void noDelayIsFree() throws Exception {
        assertEquals(0, calculator.calculateFee(0, false));
    }

    @Test
    public void twoDaysAreStillInTheGracePeriod() throws Exception {
        assertEquals(0, calculator.calculateFee(1, false));
        assertEquals(0, calculator.calculateFee(2, false));
    }

    @Test
    public void theThirdDayCostsOneEuro() throws Exception {
        assertEquals(1, calculator.calculateFee(3, false));
    }

    @Test
    public void oneEuroPerDayBeyondTheGracePeriod() throws Exception {
        assertEquals(8, calculator.calculateFee(10, false));
    }

    @Test
    public void premiumMembersPayHalfRoundedDown() throws Exception {
        assertEquals(4, calculator.calculateFee(11, true));
        assertEquals(4, calculator.calculateFee(10, true));
        assertEquals(0, calculator.calculateFee(3, true));
    }

    @Test
    public void theFeeReachesTheCapAtTwentyTwoDays() throws Exception {
        assertEquals(19, calculator.calculateFee(21, false));
        assertEquals(20, calculator.calculateFee(22, false));
        assertEquals(20, calculator.calculateFee(23, false));
        assertEquals(20, calculator.calculateFee(30, false));
    }

    @Test
    public void theCapIsAppliedAfterThePremiumDiscount() throws Exception {
        assertEquals(15, calculator.calculateFee(32, true));
        assertEquals(20, calculator.calculateFee(50, true));
    }

    @Test
    public void negativeDaysThrowInvalidDaysLate() {
        assertThrows(InvalidDaysLateException.class, () -> calculator.calculateFee(-1, false));
        assertThrows(InvalidDaysLateException.class, () -> calculator.calculateFee(-1, true));
    }

    public void setCalculator(LateFeeCalculator value){
        this.calculator=value;
    }

}
