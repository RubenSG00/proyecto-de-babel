package es.us.dp1.chess.tournament.practice.b.fees;

public class EagerLateFeeCalculator implements LateFeeCalculator {

    // Bug: the grace period is one day shorter (only the first day is free).
    @Override
    public int calculateFee(int daysLate, boolean premiumMember) throws InvalidDaysLateException {
        if (daysLate < 0) {
            throw new InvalidDaysLateException();
        }
        int fee = Math.max(0, daysLate - 1);
        if (premiumMember) {
            fee = fee / 2;
        }
        return Math.min(fee, 20);
    }

}
