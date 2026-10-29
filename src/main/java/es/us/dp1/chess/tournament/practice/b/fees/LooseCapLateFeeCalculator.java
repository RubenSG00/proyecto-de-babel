package es.us.dp1.chess.tournament.practice.b.fees;

public class LooseCapLateFeeCalculator implements LateFeeCalculator {

    // Bug: the cap is 21 euros instead of 20.
    @Override
    public int calculateFee(int daysLate, boolean premiumMember) throws InvalidDaysLateException {
        if (daysLate < 0) {
            throw new InvalidDaysLateException();
        }
        int fee = Math.max(0, daysLate - 2);
        if (premiumMember) {
            fee = fee / 2;
        }
        return Math.min(fee, 21);
    }

}
