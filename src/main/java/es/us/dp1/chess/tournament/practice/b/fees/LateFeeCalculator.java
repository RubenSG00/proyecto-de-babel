package es.us.dp1.chess.tournament.practice.b.fees;

public interface LateFeeCalculator {

    /**
     * Fee, in euros, for returning a book daysLate days after its due date: the first 2 days
     * are free; then 1 euro per day beyond them (daysLate - 2); premium members pay half of
     * that, rounded down; the result never exceeds 20 (cap applied after the discount).
     *
     * @throws InvalidDaysLateException if daysLate is negative.
     */
    int calculateFee(int daysLate, boolean premiumMember) throws InvalidDaysLateException;

}
