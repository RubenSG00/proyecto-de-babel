package es.us.dp1.chess.tournament.practice.b.fees;

public class RoundingLateFeeCalculator implements LateFeeCalculator {

    // Bug: the premium discount rounds up instead of down.
    @Override
    public int calculateFee(int daysLate, boolean premiumMember) throws InvalidDaysLateException {
        if (daysLate < 0) {
            throw new InvalidDaysLateException();
        }
        int fee = Math.max(0, daysLate - 2);
        if (premiumMember) {
            fee = (fee + 1) / 2;
        }
        return Math.min(fee, 20);
    }

}
